package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.exceptions.ProductoNoExisteException;
import com.arquitectojava.letters.api.repositories.MatchesRepository;
import com.arquitectojava.letters.api.domain.sql.Match;
import com.arquitectojava.letters.api.domain.sql.Producto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchesServiceImplTest {

    @InjectMocks
    MatchesServiceImpl matchesService;

    @Mock
    private ProductosService productosService;

    @Mock
    private ProductosTercerosService productosTercerosService;

    @Mock
    private MatchesRepository matchesRepository;

    @Test
    @DisplayName("Matches por sku cuando cuando producto no existe")
    public void test_matchesBySku_when_product_not_found() throws Exception {
        //Behavior
        String productSku = "1";

        // SUT and Assert
        Assertions.assertThrows(ProductoNoExisteException.class, () -> matchesService.matchesBySku(productSku));

        //Verify
        verify(productosService, times(1)).findBySku(productSku);
        verifyNoInteractions(matchesRepository);
    }

    @Test
    @DisplayName("Matches por sku cuando no tiene matches")
    public void test_matchesBySku_when_no_matches() throws  Exception {
        //Behavior
        String productSku = "123";

        Producto existingProduct = buildExistingLettersProduct(1, productSku);

        when(productosService.findBySku(productSku)).thenReturn(existingProduct);

        // SUT
        List<Match> matches = matchesService.matchesBySku(productSku);

        //Assert
        Assertions.assertEquals(0, matches.size());

        //Verify
        verify(productosService, times(1)).findBySku(productSku);
        verify(matchesRepository, times(1)).matches(existingProduct.getId());
    }

    @Test
    @DisplayName("Matches por sku cuando tiene matches")
    public void test_matchesBySku_when_matches() throws  Exception {
        //Behavior
        String productSku = "123";

        Producto existingProduct = buildExistingLettersProduct(1, productSku);

        Match existingMatch = buildExistingMatch(1, existingProduct.getId(), 4);

        when(productosService.findBySku(productSku)).thenReturn(existingProduct);
        when(matchesRepository.matches(existingProduct.getId())).thenReturn(Collections.singletonList(existingMatch));

        // SUT
        List<Match> matches = matchesService.matchesBySku(productSku);

        //Assert
        Assertions.assertEquals(1, matches.size());
        Assertions.assertEquals(existingMatch.getMatchId(), matches.get(0).getMatchId());

        //Verify
        verify(productosService, times(1)).findBySku(productSku);
        verify(matchesRepository, times(1)).matches(existingProduct.getId());
    }

    @Test
    @DisplayName("Reverse matches por producto_id cuando producto no existe")
    public void test_reverseMatchesByProductId_when_product_not_found() throws Exception {
        //Behavior
        int productId = 1;

        // SUT and Assert
        Assertions.assertThrows(ProductoNoExisteException.class, () -> matchesService.reverseMatchesByProductId(productId));

        //Verify
        verify(productosTercerosService, times(1)).findById(productId);
        verifyNoInteractions(matchesRepository);
    }

    @Test
    @DisplayName("Matches por producto_id cuando no tiene matches")
    public void test_reverseMatchesByProductId_when_letters_product_no_matches() throws  Exception {
        //Behavior
        int productId = 1;

        Producto existingProduct = buildExistingMarketProduct();

        when(productosTercerosService.findById(productId)).thenReturn(existingProduct);

        // SUT
        List<Match> matches = matchesService.reverseMatchesByProductId(productId);

        //Assert
        Assertions.assertEquals(0, matches.size());

        //Verify
        verify(productosTercerosService, times(1)).findById(productId);
        verify(matchesRepository, times(1)).reverseMatches(productId);
    }

    @Test
    @DisplayName("Matches por producto_id cuando tiene matches")
    public void test_reverseMatchesByProductId_when_letters_product_with_matches() throws  Exception {
        //Behavior
        int productId = 1;

        Producto existingProduct = buildExistingMarketProduct();

        Match existingMatch = buildExistingMatch(1, 2, existingProduct.getId());
        Producto lettersProduct = buildExistingLettersProduct(existingMatch.getProductoId(), "123");

        when(productosTercerosService.findById(productId)).thenReturn(existingProduct);
        when(matchesRepository.reverseMatches(productId)).thenReturn(Collections.singletonList(existingMatch));
        when(productosService.findById(existingMatch.getProductoId())).thenReturn(lettersProduct);

        // SUT
        List<Match> matches = matchesService.reverseMatchesByProductId(productId);

        //Assert
        Assertions.assertEquals(1, matches.size());
        Assertions.assertEquals(existingMatch.getProductoId(), matches.get(0).getProductoId());
        Assertions.assertEquals(lettersProduct.getSku(), matches.get(0).getProducto().getSku());

        //Verify
        verify(productosTercerosService, times(1)).findById(productId);
        verify(matchesRepository, times(1)).reverseMatches(productId);
        verify(productosService, times(1)).findById(existingMatch.getProductoId());
    }

    @Test
    @DisplayName("Actualizar matches")
    public void test_update_matches() throws  Exception {
        //Behavior
        Producto existingProduct = buildExistingLettersProduct(5, "123");

        Match existingMatch1 = buildExistingMatch(1, existingProduct.getId(), 1);
        Match existingMatch2 = buildExistingMatch(2, existingProduct.getId(), 2);

        Match receivedMatch1 = buildMatch(existingProduct.getId(), existingMatch1.getMatchId());
        Match receivedMatch3 = buildMatch(existingProduct.getId(), existingMatch2.getMatchId() + 1);

        existingProduct.setMatchesIds(Arrays.asList(receivedMatch1.getMatchId(), receivedMatch3.getMatchId()));

        when(matchesRepository.matches(existingProduct.getId())).thenReturn(Arrays.asList(existingMatch1, existingMatch2));

        // SUT
        matchesService.updateMatches(existingProduct);

        //Assert

        //Verify
        verifyNoInteractions(productosService);
        verifyNoInteractions(productosTercerosService);

        verify(matchesRepository, times(1)).matches(existingProduct.getId());
        verify(matchesRepository, times(1)).save(receivedMatch3);
        verify(matchesRepository, times(1)).delete(existingMatch2);
    }

    @Test
    @DisplayName("Actualizar reverse matches")
    public void test_update_reverseMatches() throws  Exception {
        //Behavior
        Producto existingProduct = buildExistingMarketProduct();

        Match existingMatch1 = buildExistingMatch(1, 4, existingProduct.getId());
        Match existingMatch2 = buildExistingMatch(2, 5, existingProduct.getId());

        Match receivedMatch1 = buildMatch(existingMatch1.getProductoId(), existingProduct.getId());
        Match receivedMatch2 = buildMatch(existingMatch2.getProductoId() + 1, existingProduct.getId());

        Producto lettersProductReceivedMatch1 = buildExistingLettersProduct(receivedMatch1.getProductoId(), "xyz");
        Producto lettersProductReceivedMatch2 = buildExistingLettersProduct(receivedMatch2.getProductoId(), "opq");

        when(matchesRepository.reverseMatches(existingProduct.getId())).thenReturn(Arrays.asList(existingMatch1, existingMatch2));
        when(productosService.findBySku(lettersProductReceivedMatch1.getSku())).thenReturn(lettersProductReceivedMatch1);
        when(productosService.findBySku(lettersProductReceivedMatch2.getSku())).thenReturn(lettersProductReceivedMatch2);

        existingProduct.setReverseMatchesSkus(Arrays.asList(lettersProductReceivedMatch1.getSku(), lettersProductReceivedMatch2.getSku()));

        // SUT
        matchesService.updateReverseMatches(existingProduct);

        //Assert

        //Verify
        verify(productosService, times(1)).findBySku(existingProduct.getReverseMatchesSkus().get(0));
        verify(productosService, times(1)).findBySku(existingProduct.getReverseMatchesSkus().get(1));
        verifyNoInteractions(productosTercerosService);

        verify(matchesRepository, times(1)).reverseMatches(existingProduct.getId());
        verify(matchesRepository, times(1)).save(receivedMatch2);
        verify(matchesRepository, times(1)).delete(existingMatch2);
    }

    private Match buildMatch(int productId, int matchId) {
        Match existingMatch = new Match();
        existingMatch.setProductoId(productId);
        existingMatch.setMatchId(matchId);
        return existingMatch;
    }

    private Match buildExistingMatch(int id, int productId, int matchId) {
        Match existingMatch = new Match();
        existingMatch.setId(id);
        existingMatch.setProductoId(productId);
        existingMatch.setMatchId(matchId);
        return existingMatch;
    }

    private Producto buildExistingMarketProduct() {
        Producto existingProduct = new Producto();
        existingProduct.setProveedorId(1);
        existingProduct.setSku("abc");
        existingProduct.setId(1);
        existingProduct.setNombre("BOLIGRAFO BIC CRISTAL");
        return existingProduct;
    }

    private Producto buildExistingLettersProduct(int id, String sku) {
        Producto existingProduct = new Producto();
        existingProduct.setProveedorId(0);
        existingProduct.setId(id);
        existingProduct.setSku(sku);
        existingProduct.setNombre("PRODUCTO " + sku.toUpperCase());
        return existingProduct;
    }

}