package com.arquitectojava.letters.api.domain.json;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Sale {
    private int id;
    private Client client;
    private List<Item> items;
    private Total total;
    private Payment payment;
    private Date created;

    @Data
    static public class Item {
        private int order;
        private Offering product;
        private int quantity;
    }

    @Data
    static public class Total {
        private int subtotal;
        private int iva;
        private int total;
    }

    @Data
    static public class Payment {
        private int cash;
        private int card;
        private int debt;
    }
}
