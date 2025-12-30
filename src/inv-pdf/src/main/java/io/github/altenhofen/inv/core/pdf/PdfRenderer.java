package io.github.altenhofen.inv.core.pdf;

import org.openpdf.text.Document;
import org.openpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public abstract class PdfRenderer {
    private static final DecimalFormat DF = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
    public byte[] renderDocument() {
        Document document = getDocument();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();
            draw(document, writer);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("PDF rendering failed", e);
        }
    }
    public abstract Document getDocument();

    protected abstract void draw(Document pdf, PdfWriter writer);
}

