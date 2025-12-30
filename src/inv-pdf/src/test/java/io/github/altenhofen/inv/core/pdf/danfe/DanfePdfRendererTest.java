package io.github.altenhofen.inv.core.pdf.danfe;

import io.github.altenhofen.inv.core.domain.models.*;
import io.github.altenhofen.inv.core.pdf.PdfRenderer;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class DanfePdfRendererTest {

    private static void tmpPdf(byte[] pdf) throws IOException {
        Path tmp = Files.createTempFile("danfe-header-", ".pdf");
        Files.write(tmp, pdf);

        System.out.println("PDF written to: " + tmp.toAbsolutePath());
    }
    @Test
    public void header_IsRendered() throws IOException {
        Document.Cnpj cnpj = new Document.Cnpj("99.999.999/9999-99");
        Address address = new Address(null, "BR", "RS");
        Address addressCustomer = new Address(Optional.of("12341234-00"), "BR", "RS");
        Issuer company = new Issuer(cnpj, "Nome Ficticio", "ie", address);
        Document.Cpf cpf = new Document.Cpf("999.999.999-99");
        Customer customer = new Customer(cpf, "Augusto",addressCustomer);
        List<Item> items = new ArrayList<>();
        items.add(new Item("1337", "cd disco", new BigDecimal("40"), "CX", new BigDecimal("13.37")));
        items.add(new Item("1338", "teclado", new BigDecimal("40"), "CX", new BigDecimal("53.37")));
        items.add(new Item("1339", "bebedouro", new BigDecimal("40"), "CX", new BigDecimal("103.37")));
        Invoice invoice = new Invoice("0011", "123", company, customer, items, LocalDate.now(), new BigDecimal("30000.00"));

        PdfRenderer pdf = new DanfePdfRenderer(invoice);

        byte[] byteArr = pdf.renderDocument();

        tmpPdf(byteArr);

    }

}