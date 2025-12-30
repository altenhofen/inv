package io.github.altenhofen.inv.core.pdf;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class InvoicePrinterTest {

    final static String EXAMPLE_JSON = """
                        {
              "accessKey": "43191234567890123456550010000012341000012345",
              "accessKeyFormatted": "4319 1234 5678 9012 3456 5500 1000 0012 3410 0001 2345",
              "protocol": "143190000123456",
              "natureOfOperation": "VENDA DE MERCADORIA",
            
              "emitente": {
                "name": "EXEMPLO SISTEMAS LTDA",
                "tradeName": "EXEMPLO SISTEMAS",
                "cnpj": "12.345.678/0001-90",
                "ie": "123.456.789.112",
                "address": "Rua XYZ, 123, Sala 101 - Centro",
                "neighborhood": "Centro",
                "city": "Forquetinha",
                "state": "RS",
                "cep": "95937-000",
                "phone": "(51) 3456-7890"
              },
            
              "destinatario": {
                "name": "CLIENTE EXEMPLO SA",
                "cnpj": "98.765.432/0001-10",
                "ie": "987.654.321.000",
                "address": "Av. das Empresas, 999 - Distrito Industrial",
                "neighborhood": "Distrito Industrial",
                "city": "Porto Alegre",
                "state": "RS",
                "cep": "90000-000",
                "phone": "(51) 3333-0000"
              },
            
              "items": [
                {
                  "code": "001",
                  "description": "TECLADO MECÂNICO ABNT2 PRETO",
                  "ncm": "84716053",
                  "cst": "060",
                  "cfop": "5102",
                  "unit": "UN",
                  "quantity": 2.0,
                  "unitPrice": 250.00,
                  "totalPrice": 500.00
                },
                {
                  "code": "002",
                  "description": "MOUSE ÓPTICO USB 1600DPI",
                  "ncm": "84716053",
                  "cst": "060",
                  "cfop": "5102",
                  "unit": "UN",
                  "quantity": 3.0,
                  "unitPrice": 50.00,
                  "totalPrice": 150.00
                }
              ],
            
              "totals": {
                "totalProducts": 650.00,
                "totalInvoice": 650.00,
                "icmsBase": 650.00,
                "icmsValue": 117.00,
                "ipiValue": 0.00,
                "pisValue": 14.95,
                "cofinsValue": 68.25,
                "freightValue": 0.00,
                "insuranceValue": 0.00,
                "discountValue": 0.00,
                "otherExpenses": 0.00
              },
            
              "qrCodeBase64": "",
              "barcodeBase64": ""
            }
            """;

    @Test
    void generatePdf() throws Exception {
        InvoicePrinter invoicePrinter = new InvoicePrinter();
        invoicePrinter.generatePdf(EXAMPLE_JSON, "/tmp/danfe_generatePdf.pdf");
    }
}