package com.arquitectojava.letters.api.compras.reports.domain.json;

import lombok.Data;

@Data
public class CertificadoRetencion {

    private String nombre_proveedor;
    private String nit_proveedor;
    private String anio_gravable;

    private String generado;
    private String nombre_retenedor;
    private String nit_retenedor;
    private String ciudad_retenedor;
    private String direccion_retenedor;

    private String retenido;

    @Data
    static public class Item {
        private String descripcion;
        private Integer retenido;
    }

}
