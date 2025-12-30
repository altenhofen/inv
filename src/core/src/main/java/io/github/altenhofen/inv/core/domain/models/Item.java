package io.github.altenhofen.inv.core.domain.models;

import java.math.BigDecimal;

public record Item(
        String code,
        String description,
        String ncm,
        String cst,
        String cfop,
        String unit,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
    public final static class Builder {
        private String code;
        private String description;
        private String ncm;
        private String cst;
        private String cfop;
        private String unit;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;

        public Item build() {
            return new Item(
                    code,
                    description,
                    ncm,
                    cst,
                    cfop,
                    unit,
                    quantity,
                    unitPrice,
                    totalPrice
            );
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder ncm(String ncm) {
            this.ncm = ncm;
            return this;
        }

        public Builder cfop(String cfop) {
            this.cfop = cfop;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder quantity(BigDecimal quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }
    }

}
