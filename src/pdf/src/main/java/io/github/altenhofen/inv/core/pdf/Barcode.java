package io.github.altenhofen.inv.core.pdf;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public final class Barcode {
    public static String generateCode128(String data) throws Exception {
        Code128Writer writer = new Code128Writer();
        // MOC specifies width/height requirements. Experiment with width/height here.
        BitMatrix matrix = writer.encode(data, BarcodeFormat.CODE_128, 400, 50);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}