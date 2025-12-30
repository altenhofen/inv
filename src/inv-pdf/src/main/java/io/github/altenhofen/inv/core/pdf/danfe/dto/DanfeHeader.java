package io.github.altenhofen.inv.core.pdf.danfe.dto;

import io.github.altenhofen.inv.core.domain.models.Address;
import io.github.altenhofen.inv.core.domain.models.Document;

// TODO: consider creation of Issuer class/record
public record DanfeHeader(Document issuerDocument, String issuerName, Address address) {
    public final static String TEXT = "Documento Auxiliar da Nota Fiscal de Consumidor Eletrônica";

}
