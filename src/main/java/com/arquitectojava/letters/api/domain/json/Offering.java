package com.arquitectojava.letters.api.domain.json;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Offering {

    private Integer id;
    private String name;
    private Integer stock;
    private Integer tax;
    private Integer price;
    private String image;
    private Integer package_quantity;
    private String category;
    private String color;
    private Date created;
    private Date updated;
    private List<Offering> variants;
    private List<Map<String, Object>> attributes;
    private List<Integer> matches;

}