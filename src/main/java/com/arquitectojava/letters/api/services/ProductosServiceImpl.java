package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Producto;
import com.arquitectojava.letters.api.exceptions.ProductoIvaNullException;
import com.arquitectojava.letters.api.exceptions.ProductoNoExisteException;
import com.arquitectojava.letters.api.exceptions.ProductoPrecioInvalidoException;
import com.arquitectojava.letters.api.repositories.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class ProductosServiceImpl implements ProductosService {

    protected ProductosRepository productosRepository;

    protected ApplicationEventPublisher publisher;

    public ProductosServiceImpl(@Autowired ProductosRepository productosRepository){
        this.productosRepository = productosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productosRepository.lettersProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(int id) {
        return productosRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findBySku(String sku) {
        return productosRepository.lettersProductBySku(sku);
    }

    @Override
    public Producto save(Producto producto) {
        if (producto.getIsParent() != 1 && producto.getIva() == null) throw new ProductoIvaNullException(producto);
        if (producto.getIsParent() != 1 && (producto.getPrecio() == null || producto.getPrecio() <= 0)) throw new ProductoPrecioInvalidoException(producto);
        Producto existing = findBySku(producto.getSku());
        if (existing != null){
            updateExistingFromReceived(producto, existing);
            updateVariants(producto, existing);
            return productosRepository.save(existing);
        } else {
            producto.setSku(productosRepository.nextLettersProductSku());
            producto.setProveedorId(0);
            producto.setEstado(1);
            return productosRepository.save(producto);
        }
    }

    @Override
    public Producto saveImage(Producto producto) {
        Producto existing = findBySku(producto.getSku());
        if (existing != null){
            existing.setImagen(producto.getImagen());
            return productosRepository.save(existing);
        } else {
            throw new ProductoNoExisteException(producto);
        }
    }

    protected void updateVariants(Producto producto, Producto existing) {
        if (producto.getVariantes() == null || existing.getVariantes() == null) return;
        int productVariants = producto.getVariantes().size();
        int existingVariants = existing.getVariantes().size();

        if (productVariants + existingVariants == 0) return;

        Map<String, Producto> existingVariantsBySku = new HashMap<>();
        existing.getVariantes().stream().forEach(ev -> existingVariantsBySku.put(ev.getSku(), ev));

        for (Producto variant : producto.getVariantes()){
            Producto existingVariant = existingVariantsBySku.get(variant.getSku());
            if (existingVariant != null){
                updateExistingFromReceived(variant, existingVariant);
                existingVariantsBySku.remove(variant.getSku());
            } else {
                variant.setSku(productosRepository.nextLettersProductSku());
                variant.setProveedorId(0);
                variant.setEstado(1);
                existing.getVariantes().add(variant);
            }
        }

        for (Producto existingVariantNotReceived : existingVariantsBySku.values()){
            existing.getVariantes().remove(existingVariantNotReceived);
            existingVariantNotReceived.setEntitledItem(null);
        }
    }

    protected void updateExistingFromReceived(Producto producto, Producto existing) {
        existing.setNombre(producto.getNombre());
        existing.setMarca(producto.getMarca());
        existing.setCantidadPorEmpaque(producto.getCantidadPorEmpaque());
        existing.setCategoria(producto.getCategoria());
        existing.setIva(producto.getIva());
        existing.setPrecio(producto.getPrecio());
        existing.setInventario(producto.getInventario());
        existing.setImagen(producto.getImagen());
        existing.setColor(producto.getColor());
        existing.setEstado(1);
    }
}
