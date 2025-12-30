package io.github.altenhofen.inv.core.domain.models;

import java.math.BigDecimal;

public record Item(String code, String description, BigDecimal qty, String unit, BigDecimal unitPrice) {
    public BigDecimal totalPrice() {
        return qty.multiply(unitPrice);
    }
}
