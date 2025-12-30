package io.github.altenhofen.inv.core.domain.models;

import java.util.Optional;

public record Address(Optional<String> cep, String country, String uf) {
    public String getConventionalAddress() {
        return "Av da Tecnologia, 030, Centro, Rio de Janeiro, RJ";
    }
}
