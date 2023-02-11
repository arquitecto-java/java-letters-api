package com.arquitectojava.letters.api.domain.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class JsonToAtributosConverter implements AttributeConverter<List<Atributo>, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonToAtributosConverter.class);

    @Override
    public List<Atributo> convertToEntityAttribute(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(attribute, objectMapper.getTypeFactory().constructCollectionType(List.class, Atributo.class));
        } catch (IOException e) {
            LOGGER.error("Convert error while trying to convert string(JSON) to map data structure.");
        }
        return null;
    }

    @Override
    public String convertToDatabaseColumn(List<Atributo> dbData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(dbData);
        } catch (JsonProcessingException e) {
            LOGGER.error("Could not convert map to json string.");
            return null;
        }
    }
}