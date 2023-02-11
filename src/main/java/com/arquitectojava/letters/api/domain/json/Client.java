package com.arquitectojava.letters.api.domain.json;

import lombok.Data;

import java.util.Date;

@Data
public class Client {
    private Integer id;
    private String fname;
    private String lname;
    private String doc_id;
    private String phone;
    private String instagram;
    private String email;
    private String address;
    private Date created;
}
