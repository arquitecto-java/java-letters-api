package com.arquitectojava.letters.api.events;

import com.arquitectojava.letters.api.domain.sql.Producto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoConMatchesActualizadoEvent {

    private Producto producto;

}
