package com.arquitectojava.letters.api.domain.json;

import lombok.Data;

import java.util.Date;

@Data
public class Supplier {
    private int id;
    private String company_name;
    private String fname;
    private String lname;
    //private String cc;
    //private String nit;
    //private String email;
    //private String bank;
    //private String account_type;
    //private String account_number;
    //private String phone;
    //private String address;
    //private String city;

    private Date created;
}
