package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Producto;
import com.arquitectojava.letters.api.events.ProductoConMatchesActualizadoEvent;
import com.arquitectojava.letters.api.exceptions.ProductoMercadoDuplicadoPorSKUException;
import com.arquitectojava.letters.api.exceptions.ProveedorNoValidoProductoMercadoException;
import com.arquitectojava.letters.api.repositories.ProductosRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = false)
public class ProductosTercerosServiceImpl implements ProductosTercerosService {

    protected ProductosRepository productosRepository;

    protected ApplicationEventPublisher publisher;

    public ProductosTercerosServiceImpl(@Autowired ProductosRepository productosRepository, @Autowired ApplicationEventPublisher publisher){
        this.productosRepository = productosRepository;
        this.publisher = publisher;
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(int id) {
        return productosRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productosRepository.marketProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findBySku(String sku, int proveedorId) {
        return productosRepository.marketProductBySku(sku, proveedorId);
    }

    @Override
    public Producto save(Producto producto) {
        Producto saved = null;

        if (producto.getProveedorId() == null || producto.getProveedorId() <= 0)
            throw new ProveedorNoValidoProductoMercadoException(producto.getProveedorId());

        Producto productoExistente = null;
        if (!StringUtils.isBlank(producto.getSku())) {
            Producto existentePorSku = findBySku(producto.getSku(), producto.getProveedorId());
            if (existentePorSku != null && !Objects.equals(producto.getId(), existentePorSku.getId())){
                throw new ProductoMercadoDuplicadoPorSKUException(existentePorSku.getSku(), producto.getProveedorId());
            }
            productoExistente = existentePorSku;
        }

        if (productoExistente == null && producto.getId() != null){
            productoExistente = productosRepository.findById(producto.getId()).get();
        }

        if (productoExistente != null){
            updateExistingFromReceived(producto, productoExistente);
            saved = productosRepository.save(productoExistente);
        } else {
            saved = productosRepository.save(producto);
        }

        if (producto.getMatchesIds() != null || producto.getReverseMatchesSkus() != null){
            publisher.publishEvent(ProductoConMatchesActualizadoEvent.builder().producto(producto).build());
        }

        return saved;
    }

    protected void updateExistingFromReceived(Producto producto, Producto productoExistente) {
        productoExistente.setSku(producto.getSku());
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setMarca(producto.getMarca());
        productoExistente.setCantidadPorEmpaque(producto.getCantidadPorEmpaque());
        productoExistente.setCategoria(producto.getCategoria());
        productoExistente.setIva(producto.getIva());
        productoExistente.setPrecio(producto.getPrecio());
        //productoExistente.setInventario(producto.getInventario());
        productoExistente.setImagen(producto.getImagen());
        productoExistente.setProveedorId(producto.getProveedorId());
    }
}
