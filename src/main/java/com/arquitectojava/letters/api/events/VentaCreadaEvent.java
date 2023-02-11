package com.arquitectojava.letters.api.events;

import com.arquitectojava.letters.api.domain.sql.Venta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VentaCreadaEvent {
    private Venta venta;
}
