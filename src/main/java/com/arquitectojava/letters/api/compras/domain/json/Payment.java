package com.arquitectojava.letters.api.compras.domain.json;

import lombok.Data;

import java.util.Date;

public class Payment {

    private Integer id;
    private SupplierBank supplier;
    private Invoice invoice;
    private Date created;
    private Date paid;
    private Integer in_cash;
    private Integer by_check;
    private String check_number;
    private Integer by_account;
    private SupplierBank account_bank;
    private String account_number;
    private Integer withheld;

    @Data
    static public class SupplierBank {
        private int id;
        private String name;
    }

    @Data
    static public class Invoice {
        private int id;
        private String invoiceNumber;
    }

}