package com.arquitectojava.letters.api.domain.json;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultJSON {

    private String message;

}