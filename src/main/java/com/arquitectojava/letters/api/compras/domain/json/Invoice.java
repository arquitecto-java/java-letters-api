package com.arquitectojava.letters.api.compras.domain.json;

import lombok.Data;

import java.util.Date;

public class Invoice {

    private Integer id;
    private Supplier supplier;
    private String invoice_number;
    private Date created;
    private Date date;
    private Date annulled;
    private Integer base;
    private Integer discount;
    private Integer vat;
    private Integer total;
    private Integer paid;
    private Integer withheld;

    @Data
    static public class Supplier {
        private int id;
        private String name;
    }
}