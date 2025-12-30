package io.github.altenhofen.inv.core.domain.models;

import io.github.altenhofen.inv.core.domain.formatters.AbstractFormatter;
import io.github.altenhofen.inv.core.domain.formatters.CnpjFormatter;
import io.github.altenhofen.inv.core.domain.formatters.CpfFormatter;

public abstract sealed class Document
        permits Document.Cnpj, Document.Cpf {

    private final String value;
    private final AbstractFormatter<String> formatter;

    protected Document(String value, AbstractFormatter<String> formatter) {
        this.value = value;
        this.formatter = formatter;
    }

    public final String getFormattedValue() {
        return formatter.format(value);
    }

    public String getValue() {
        return this.value;
    }


    public abstract boolean isValid();

    public static final class Cnpj extends Document {

        private static final AbstractFormatter<String> FORMATTER = new CnpjFormatter();

        public Cnpj(String value) {
            super(value, FORMATTER);
        }

        @Override
        public boolean isValid() {
            // TODO: implement CNPJ validation
            return false;
        }
    }

    public static final class Cpf extends Document {

        private static final AbstractFormatter<String> FORMATTER = new CpfFormatter();

        public Cpf(String value) {
            super(value, FORMATTER);
        }

        @Override
        public boolean isValid() {
            // TODO: implement CPF validation
            return false;
        }
    }
}

