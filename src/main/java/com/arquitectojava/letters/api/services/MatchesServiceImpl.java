package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Match;
import com.arquitectojava.letters.api.domain.sql.Producto;
import com.arquitectojava.letters.api.events.ProductoConMatchesActualizadoEvent;
import com.arquitectojava.letters.api.exceptions.ProductoNoExisteException;
import com.arquitectojava.letters.api.repositories.MatchesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = false)
public class MatchesServiceImpl implements MatchesService {

    protected ProductosService productosService;
    protected ProductosTercerosService productosTercerosService;
    protected MatchesRepository matchesRepository;

    MatchesServiceImpl(@Autowired ProductosService productosService, @Autowired ProductosTercerosService productosTercerosService, @Autowired MatchesRepository matchesRepository){
        this.productosService = productosService;
        this.productosTercerosService = productosTercerosService;
        this.matchesRepository = matchesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> reverseMatchesByProductId(int productoId) {
        Producto p = productosTercerosService.findById(productoId);

        if (p == null) throw new ProductoNoExisteException(productoId);

        List<Match> reverseMatches = matchesRepository.reverseMatches(productoId);
        reverseMatches.forEach(rm -> {
            rm.setProducto(productosService.findById(rm.getProductoId()));
        });

        return reverseMatches;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> matchesBySku(String sku) {
        Producto p = productosService.findBySku(sku);

        if (p == null) throw new ProductoNoExisteException(sku);

        return matchesRepository.matches(p.getId());
    }

    @Override
    public void updateMatches(Producto p) {
        List<Integer> matches = p.getMatchesIds();

        //Consultar matches actuales
        List<Match> existingMatches = matchesRepository.matches(p.getId());
        Map<Integer, Match> existingMatchesMap = new HashMap<>();
        existingMatches.forEach(m -> existingMatchesMap.put(m.getMatchId(), m));

        //Iterar matches recibidos
        matches.forEach(matchId -> {
            //Si no existe, crear
            if (!existingMatchesMap.containsKey(matchId)){
                matchesRepository.save(buildMatch(p, matchId));
            } else {
                //Si existe y es recibido, quitar de map porque los que queden serán eliminados
                existingMatchesMap.remove(matchId);
            }
        });

        //Los que existen pero no se recibieron se eliminan
        existingMatchesMap.forEach((matchId, m) -> {
            matchesRepository.delete(m);
        });
    }

    @Override
    public void updateReverseMatches(Producto p) {
        List<String> skusReverseMatches = p.getReverseMatchesSkus();
        List<Integer> reverseMatches = skusReverseMatches.stream()
                .map(sku -> productosService.findBySku(sku).getId())
                .collect(Collectors.toList());

        //Consultar matches actuales
        List<Match> existingReverseMatches = matchesRepository.reverseMatches(p.getId());
        Map<Integer, Match> existingReverseMatchesMap = new HashMap<>();
        existingReverseMatches.forEach(m -> existingReverseMatchesMap.put(m.getProductoId(), m));

        //Iterar matches recibidos
        reverseMatches.forEach(productId -> {
            //Si no existe, crear
            if (!existingReverseMatchesMap.containsKey(productId)){
                matchesRepository.save(buildReverseMatch(p, productId));
            } else {
                //Si existe y es recibido, quitar de map porque los que queden serán eliminados
                existingReverseMatchesMap.remove(productId);
            }
        });

        //Los que existen pero no se recibieron se eliminan
        existingReverseMatchesMap.forEach((productId, m) -> {
            matchesRepository.delete(m);
        });
    }

    protected Match buildMatch(Producto p, Integer matchId) {
        Match m = new Match();
        m.setProductoId(p.getId());
        m.setMatchId(matchId);
        return m;
    }

    protected Match buildReverseMatch(Producto p, Integer matchId) {
        Match m = new Match();
        m.setMatchId(p.getId());
        m.setProductoId(matchId);
        return m;
    }

    @EventListener
    public void updateVentaKit(ProductoConMatchesActualizadoEvent event) {
        if (event.getProducto().getProveedorId() == 0){
            updateMatches(event.getProducto());
        } else {
            updateReverseMatches(event.getProducto());
        }
    }
}
