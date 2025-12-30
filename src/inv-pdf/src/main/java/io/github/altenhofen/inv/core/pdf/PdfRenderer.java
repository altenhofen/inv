package io.github.altenhofen.inv.core.pdf;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.openpdf.text.Document;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Image;
import org.openpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public abstract class PdfRenderer {
    protected static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 7);
    protected static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD);
    protected static final Font FONT_HEADER = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);
    protected static final Font FONT_SMALL = FontFactory.getFont(FontFactory.HELVETICA, 6); // Para a chave de acesso e URL
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

    protected Image generateQrCode(String texto, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // b&w bitmatrix
        BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, width, height);

        // bytestream
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        // create openpdf image
        Image image = Image.getInstance(pngData);
        image.scaleToFit(80, 80); // points
        return image;
    }

    protected String formatAccessKey(String chave) {
        // 4 digit group: 0000 0000 ...
        if (chave == null || chave.length() != 44) return chave;
        return chave.replaceAll("(\\d{4})(?=\\d)", "$1 ");
    }
}

