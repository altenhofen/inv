package io.github.altenhofen.inv.core.domain.models;

public record Issuer(
    String name,
    String tradeName,
    String cnpj,
    String ie,
    String address,
    String neighborhood,
    String city,
    String state,
    String cep,
    String phone
) {}

