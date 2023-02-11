package com.arquitectojava.letters.api.domain.json;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MarketProduct {

    private Integer id;
    private int supplier_id;
    private String sku;
    private String name;
    private Integer price;
    private String image;
    private Integer package_quantity;
    private String category;
    private Date created;
    private Date updated;

    private List<String> reverse_matches;

}