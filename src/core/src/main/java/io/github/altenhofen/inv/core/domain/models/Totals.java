package io.github.altenhofen.inv.core.domain.models;

import java.math.BigDecimal;

public record Totals(
        BigDecimal totalProducts,
        BigDecimal totalInvoice,
        BigDecimal icmsBase,
        BigDecimal icmsValue,
        BigDecimal ipiValue,
        BigDecimal pisValue,
        BigDecimal cofinsValue,
        BigDecimal freightValue,
        BigDecimal insuranceValue,
        BigDecimal discountValue,
        BigDecimal otherExpenses
) {}
