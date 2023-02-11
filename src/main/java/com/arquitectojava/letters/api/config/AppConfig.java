package com.arquitectojava.letters.api.config;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import com.arquitectojava.letters.api.domain.json.*;
import com.arquitectojava.letters.api.domain.sql.*;
import com.arquitectojava.lettersservices.domain.json.*;
import com.arquitectojava.letters.api.domain.json.Sale.Item;
import com.arquitectojava.lettersservices.domain.sql.*;
import ma.glasnost.orika.*;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("com.arquitectojava.lettersservices")
public class AppConfig {

    protected MapperFactory posMapperFactory = new DefaultMapperFactory.Builder().build();

    protected MapperFactory marketMapperFactory = new DefaultMapperFactory.Builder().build();

    @Bean
    public MapperFacade posMapperFactory(){
        posMapperFactory.classMap(Producto.class, Offering.class)
                .field("sku", "id")
                .field("nombre", "name")
                .field("inventario", "stock")
                .field("iva", "tax")
                .field("precio", "price")
                .field("imagen", "image")
                .field("cantidadPorEmpaque", "package_quantity")
                .field("categoria", "category")
                .field("color", "color")
                .field("creado", "created")
                .field("actualizado", "updated")
                .field("atributos", "attributes")
                //.field("variantes", "variants") //Se usa custom mapper para controlar recursividad, productos hijos no deben usar variantes porque causa que hibernate genere un select por cada producto para hacer join y buscar hijos
                .customize(new CustomMapper<Producto, Offering>() {
                    @Override
                    public void mapAtoB(Producto producto, Offering offering, MappingContext context) {
                        if (1 == producto.getIsParent()){
                            offering.setVariants(producto.getVariantes().stream()
                                    .map(p -> this.mapperFacade.map(p, Offering.class))
                                    .collect(Collectors.toList()));
                        }
                    }

                    @Override
                    public void mapBtoA(Offering offering, Producto producto, MappingContext context) {
                        if (offering.getVariants() != null) {
                            producto.setVariantes(offering.getVariants().stream()
                                    .map(p -> this.mapperFacade.map(p, Producto.class))
                                    .collect(Collectors.toList()));
                        }
                    }
                })
                .register();

        posMapperFactory.classMap(Atributo.class, Map.class)
                .field("key", "['key']")
                .field("value", "['value']")
                .register();

        posMapperFactory.classMap(Cliente.class, Client.class)
                .field("docId", "doc_id")
                .byDefault()
                .register();

        posMapperFactory.classMap(DetalleVenta.class, Sale.Item.class)
                .field("orden", "order")
                .field("producto", "product")
                .field("cantidad", "quantity")
                .customize(new CustomMapper<DetalleVenta, Item>() {
                    @Override
                    public void mapBtoA(Item item, DetalleVenta detalleVenta, MappingContext context) {
                        detalleVenta.setTotal(detalleVenta.getProducto().getPrecio() * detalleVenta.getCantidad());
                        detalleVenta.setValorSinIva((int) Math.ceil(detalleVenta.getTotal() / (1 + (detalleVenta.getProducto().getIva() / 100.))));
                        detalleVenta.setIva(detalleVenta.getTotal() - detalleVenta.getValorSinIva());
                    }
                })
                .register();

        posMapperFactory.classMap(Venta.class, Sale.class)
                .field("clienteId", "client.id")
                .field("cliente", "client")
                .field("valorSinIva", "total.subtotal")
                .field("iva", "total.iva")
                .field("total", "total.total")
                .field("efectivo", "payment.cash")
                .field("tarjeta", "payment.card")
                .field("descuento", "payment.debt")
                .field("creado", "created")
                .field("detallesVenta", "items")
                .byDefault()
                .register();

        return posMapperFactory.getMapperFacade();
    }

    @Bean
    public MapperFacade marketMapperFactory(){
        marketMapperFactory.classMap(Producto.class, MarketProduct.class)
                .byDefault()
                .field("proveedorId", "supplier_id")
                .field("nombre", "name")
                //.field("inventario", "stock")
                //.field("iva", "tax")
                .field("precio", "price")
                .field("imagen", "image")
                .field("cantidadPorEmpaque", "package_quantity")
                .field("categoria", "category")
                .field("creado", "created")
                .field("actualizado", "updated")
                .field("reverseMatchesSkus", "reverse_matches")
                .register();

        marketMapperFactory.classMap(Tercero.class, Supplier.class)
                .field("nombreEmpresa", "company_name")
                .field("nombres", "fname")
                .field("apellidos", "lname")
                //.field("banco", "bank")
                //.field("tipoCuenta", "account_type")
                //.field("numeroCuenta", "account_number")
                //.field("telefono", "phone")
                //.field("direccion", "address")
                //.field("ciudad", "city")
                .field("creado", "created")
                .byDefault()
                .register();

        return posMapperFactory.getMapperFacade();
    }

    @Bean
    public BoundMapperFacade<Producto, Offering> productoToOfferingBoundMapperFacade(){
        BoundMapperFacade<Producto, Offering> boundMapper =
                posMapperFactory.getMapperFacade(Producto.class, Offering.class);

        return boundMapper;
    }

    @Bean
    public BoundMapperFacade<Cliente, Client> clienteToClientBoundMapperFacade(){
        BoundMapperFacade<Cliente, Client> boundMapper =
                posMapperFactory.getMapperFacade(Cliente.class, Client.class);

        return boundMapper;
    }

    @Bean
    public BoundMapperFacade<Venta, Sale> ventaToSaleBoundMapperFacade(){
        BoundMapperFacade<Venta, Sale> boundMapper =
                posMapperFactory.getMapperFacade(Venta.class, Sale.class);

        return boundMapper;
    }

    @Bean
    public BoundMapperFacade<DetalleVenta, Item> detalleVentaToItemBoundMapperFacade(){
        BoundMapperFacade<DetalleVenta, Item> boundMapper =
                posMapperFactory.getMapperFacade(DetalleVenta.class, Item.class);

        return boundMapper;
    }

    @Bean
    public BoundMapperFacade<Tercero, Supplier> terceroToSupplierBoundMapperFacade(){
        BoundMapperFacade<Tercero, Supplier> boundMapper =
                marketMapperFactory.getMapperFacade(Tercero.class, Supplier.class);

        return boundMapper;
    }

    @Bean
    public BoundMapperFacade<Producto, MarketProduct> productoToMarketProductBoundMapperFacade(){
        BoundMapperFacade<Producto, MarketProduct> boundMapper =
                marketMapperFactory.getMapperFacade(Producto.class, MarketProduct.class);

        return boundMapper;
    }
}
