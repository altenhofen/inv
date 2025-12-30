package io.github.altenhofen.inv.core.domain.viewmodel;

import io.github.altenhofen.inv.core.domain.models.Item;
import io.github.altenhofen.inv.core.domain.models.Receiver;
import io.github.altenhofen.inv.core.domain.models.Totals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record DanfeViewModel(
        String accessKey,     // 44 digits
        String accessKeyFormatted, // With spaces
        DanfeIde ide
//        String protocol,
//        Issuer emitente,
//        Receiver destinatario,
//        List<Item> items,
//        Totals totals,
//        String qrCodeBase64,  // Generated before rendering
//        String barcodeBase64  // Generated before rendering
) {


    public static final class Builder {
        private String accessKey;     // 44 digits
        private String accessKeyFormatted; // With spaces
        private String protocol;
        private String natureOfOperation;
        // --
        private String issuerName;
        private String model;
        private String series;
        private String nfNumber;
        private LocalDate emissionDate;
        private LocalDate ioDate;
        private String issuerCnpj;
        private Finality finality;
        private boolean isHomolog = false;
        // \--
        private Receiver destinatario;
        private List<Item> items = new ArrayList<>();
        private Totals totals;
        private String qrCodeBase64;  // Generated before rendering
        private String barcodeBase64; // Generated before rendering
        private OpType ioType;
        private String enviromentType;

        public Builder() {
        }

        public DanfeViewModel build() {
//            Issuer issuer = new Issuer(issuerName,
//                    issuerName,
//                    issuerCnpj,
//                    "ie",
//                    "address",
//                    "bairro",
//                    "cidade",
//                    "RS",
//                    "90000000",
//                    "51000000000");

            DanfeIde danfeIde = new DanfeIde(
                    this.natureOfOperation,
                    this.model,
                    this.series,
                    this.nfNumber,
                    this.emissionDate,
                    this.ioDate,
                    this.ioType,
                    this.finality,
                    this.isHomolog
            );
            return new DanfeViewModel(
                    this.accessKey,
                    this.accessKeyFormatted,
                    danfeIde
            );
        }

        public Builder addItem(Item item) {
            this.items.add(item);
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder isHomolog(boolean isHomolog) {
            this.isHomolog = isHomolog;
            return this;
        }

        public Builder series(String series) {
            this.series = series;
            return this;
        }

        public Builder ioType(String ioType) {
            this.ioType = OpType.fromCode(ioType);
            return this;
        }

        public Builder nfNumber(String nfNumber) {
            this.nfNumber = nfNumber;
            return this;
        }

        public Builder emissionDate(String emissionDate) {
            this.emissionDate = LocalDate.parse(emissionDate);
            return this;
        }

        public Builder ioDate(String ioDate) {
            this.ioDate = LocalDate.parse(ioDate);
            return this;
        }

        public Builder emitenteName(String emitenteName) {
            this.issuerName = emitenteName;
            return this;
        }

        public Builder emitenteCnpj(String emitenteCnpj) {
            this.issuerCnpj = emitenteCnpj;
            return this;
        }

        public Builder accessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        public Builder natureOfOperation(String natureOfOperation) {
            this.natureOfOperation = natureOfOperation;
            return this;
        }

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder finality(String finality) {
            this.finality = Finality.fromCode(finality);
            return this;
        }
    }
}


record DanfeIde(
        String operationNature,
        String model,
        String series,
        String number,
        LocalDate emissionDate,
        LocalDate ioDate,
        OpType type,
        Finality finality,
        boolean isHomolog
) {
}


enum OpType {
    ENTRANCE(0),
    EXIT(1);

    private final int code;

    OpType(int code) {
        this.code = code;
    }

    public static OpType fromCode(String value) {
        int code = Integer.parseInt(value);
        for (OpType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("Invalid tpNF: " + value);
    }
}


enum Finality {
    NORMAL(1),
    COMPLEMENTARY(2),
    ADJUSTMENT(3),
    RETURN(4);

    private final int code;

    Finality(int code) {
        this.code = code;
    }

    public static Finality fromCode(String value) {
        int code = Integer.parseInt(value);
        for (Finality f : values()) {
            if (f.code == code) return f;
        }
        throw new IllegalArgumentException("Invalid finNFe: " + value);
    }
}
