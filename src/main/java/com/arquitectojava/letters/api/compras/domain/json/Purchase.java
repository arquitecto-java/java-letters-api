package com.arquitectojava.letters.api.compras.domain.json;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Purchase {

    private Integer id;
    private Supplier supplier;
    private List<Concept> concepts;
    private String description;
    private Date created;
    private Date purchased;
    private Date received;
    private Integer transport_cost;
    private Integer paid;

    @Data
    static public class Supplier {
        private int id;
        private String name;
    }

    @Data
    @Builder
    static public class Concept {
        private int id;
        private String name;
    }

}