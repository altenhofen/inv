package io.github.altenhofen.inv.core.pdf.danfe;

import io.github.altenhofen.inv.core.domain.models.Invoice;
import io.github.altenhofen.inv.core.domain.models.Item;
import io.github.altenhofen.inv.core.pdf.PdfRenderer;
import io.github.altenhofen.inv.core.pdf.danfe.dto.DanfeHeader;
import org.openpdf.text.*;
import org.openpdf.text.pdf.*;

// TODO: understand which methods and things can go to the abstract class
// THIS IS VERY EXPERIMENTAL, just to see what i can roll
public class DanfePdfRenderer extends PdfRenderer {

    private final Invoice invoice;

    public DanfePdfRenderer(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public Document getDocument() {
        float width = 226f;
        float height = 10000f; // just so it doesnt break our pages
        Rectangle pageSize = new Rectangle(width, height);

        return new Document(pageSize, 7, 7, 7, 7);
    }

    @Override
    protected void draw(Document document, PdfWriter writer) {
        renderHeader(document);
        addSeparator(document);
        renderItems(document);
        addSeparator(document);
        addTotal(document);
        addQrCodeFooter(document);
    }

    // -----------------------------------------------------------------------
    // SPEC 3.1.1 (HEADER)
    // -----------------------------------------------------------------------
    private void renderHeader(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        // Reconsider DTO

        DanfeHeader danfeHeader = new DanfeHeader(invoice.getIssuer().document(), invoice.getIssuer().name(), invoice.getIssuer().address());
        final String cnpj = danfeHeader.issuerDocument().getFormattedValue();
        final String razaoSocial = danfeHeader.issuerName();
        final String conventionalAddress = danfeHeader.address().getConventionalAddress();

        // line 1: CNPJ and Razão Social
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk("CNPJ: " + cnpj + " ", FONT_NORMAL));
        p1.add(new Chunk(razaoSocial, DanfePdfRenderer.FONT_BOLD));
        p1.setAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell(p1);
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell1);

        // Line 2: Endereço
        PdfPCell cell2 = new PdfPCell(new Phrase(conventionalAddress, FONT_NORMAL));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);

        // Line 3: Texto Fixo
        PdfPCell cell3 = new PdfPCell(new Phrase(DanfeHeader.TEXT, FONT_NORMAL));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell3);

        document.add(table);
    }

    // -----------------------------------------------------------------------
    // SPEC 3.1.2 (ITEMS)
    // -----------------------------------------------------------------------
    private void renderItems(Document doc) throws DocumentException {
        // columns: Código, Descrição, Qtde, UN, Vl Unit, Vl Total
        float[] columnWidths = {2f, 4f, 1.2f, 1f, 1.8f, 1.8f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        // item table header
        table.addCell(createCell("Código", FONT_BOLD, Element.ALIGN_LEFT));
        table.addCell(createCell("Descrição", FONT_BOLD, Element.ALIGN_LEFT));
        table.addCell(createCell("Qtde", FONT_BOLD, Element.ALIGN_RIGHT));
        table.addCell(createCell("UN", FONT_BOLD, Element.ALIGN_CENTER));
        table.addCell(createCell("Vl Unit", FONT_BOLD, Element.ALIGN_RIGHT));
        table.addCell(createCell("Vl Total", FONT_BOLD, Element.ALIGN_RIGHT));

        // render items
        for (Item item : this.invoice.getItems()) {
            // todo renderer, factory creative;!!
            addItemRow(table, item.code(), item.description(), item.qty().toString(),  item.unit(), item.unitPrice().toString(), item.totalPrice().toString());
        }

        doc.add(table);
    }


    // -----------------------------------------------------------------------
    // total & payment
    // -----------------------------------------------------------------------
    private void addTotal(Document doc) throws DocumentException {
        PdfPTable table = new PdfPTable(2); // Coluna Label | Coluna Valor
        table.setWidthPercentage(100);
        float[] widths = {3f, 1f}; // Mais espaço para texto, menos para valor
        table.setWidths(widths);

        // total item qty
        table.addCell(createCell("Qtde. total de itens", FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell("4", FONT_NORMAL, Element.ALIGN_RIGHT));

        // total value
        table.addCell(createCell("Valor total R$", FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell(invoice.total().toString(), FONT_NORMAL, Element.ALIGN_RIGHT));

        // discount
        table.addCell(createCell("Desconto R$", FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell(invoice.discount().toString(),  FONT_NORMAL, Element.ALIGN_RIGHT));

        // to pay
        table.addCell(createCell("Valor a Pagar R$", FONT_BOLD, Element.ALIGN_LEFT));
        table.addCell(createCell(invoice.totalWithDiscount().toString(), FONT_BOLD, Element.ALIGN_RIGHT));

        // spacing
        PdfPCell empty = new PdfPCell(new Phrase(" "));
        empty.setBorder(Rectangle.NO_BORDER);
        empty.setColspan(2);
        table.addCell(empty);

        // payment
        table.addCell(createCell("FORMA PAGAMENTO", FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell("VALOR PAGO R$", FONT_NORMAL, Element.ALIGN_RIGHT));

        table.addCell(createCell("Dinheiro", FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell(invoice.getValuePaid().toString(), FONT_NORMAL, Element.ALIGN_RIGHT));

        table.addCell(createCell("Troco R$", FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell(invoice.exchange().toString(), FONT_NORMAL, Element.ALIGN_RIGHT));

        doc.add(table);
    }

     private void addQrCodeFooter(Document doc) throws DocumentException {
         PdfPTable tableConsulta = new PdfPTable(1);
         tableConsulta.setWidthPercentage(100);

         PdfPCell cellMsg = createCell("Consulte pela Chave de Acesso em", FONT_NORMAL, Element.ALIGN_CENTER);
         tableConsulta.addCell(cellMsg);

         // URL (TODO: find it, document layout, etc);
         PdfPCell cellUrl = createCell("www.fazenda.rj.gov.br/nfce/consulta", FONT_NORMAL, Element.ALIGN_CENTER);
         tableConsulta.addCell(cellUrl);

         // Chave de Acesso (Formatada com espaços para leitura fácil)
         String chaveAcesso = "33150300000000000001650010000000011000000001";
         String chaveFormatada = formatAccessKey(chaveAcesso);
         PdfPCell cellChave = createCell(chaveFormatada, FONT_NORMAL, Element.ALIGN_CENTER);
         tableConsulta.addCell(cellChave);

         doc.add(tableConsulta);
         doc.add(new Paragraph(" ", FONT_SMALL)); // Espacinho

         // 2column table
         // column 1 (35%): QR Code
         // colukmn 2 (65%): texts (Consumidor, Protocolo)
         PdfPTable tableDados = new PdfPTable(new float[]{3.5f, 6.5f});
         tableDados.setWidthPercentage(100);

         // --> left col: IMAGEM DO QR CODE
         // URL SEFAZ (TODO: get theright ones)
         String urlQrCode = "http://www.fazenda.rj.gov.br/nfce/qrcode?p=" + chaveAcesso + "|2|1|1|... (restante dos parametros)";

         try {
             Image qrImage = generateQrCode(urlQrCode, 100, 100);
             PdfPCell cellQr = new PdfPCell(qrImage);
             cellQr.setBorder(Rectangle.NO_BORDER);
             cellQr.setHorizontalAlignment(Element.ALIGN_CENTER); //center qrcode on its column
             cellQr.setVerticalAlignment(Element.ALIGN_TOP);
             tableDados.addCell(cellQr);
         } catch (Exception e) {
             // fallback just so it doesnt crash
             tableDados.addCell(createCell("[Erro QR]", FONT_NORMAL, Element.ALIGN_CENTER));
         }

         // --> right col: DADOS DO CONSUMIDOR E PROTOCOLO
         PdfPCell cellInfo = new PdfPCell();
         cellInfo.setBorder(Rectangle.NO_BORDER);
         cellInfo.setHorizontalAlignment(Element.ALIGN_LEFT);

         // build consumer text
         Paragraph pConsumidor = new Paragraph();
         pConsumidor.setLeading(9f); // Altura da linha apertada
         pConsumidor.add(new Chunk("CONSUMIDOR - ", FONT_BOLD));
         pConsumidor.add(new Chunk(this.invoice.getCustomer().document().getFormattedValue() + " ", FONT_NORMAL));
         pConsumidor.add(new Chunk(this.invoice.getCustomer().address().getConventionalAddress(), FONT_NORMAL));
         cellInfo.addElement(pConsumidor);

         // NFC-e (Número, Série, Data)
         Paragraph pNfce = new Paragraph();
         pNfce.setLeading(10f);
         pNfce.setSpacingBefore(4f);
         pNfce.add(new Chunk("NFC-e nº 000000001  Série 001  10/03/2015 15:03:53", FONT_BOLD));
         cellInfo.addElement(pNfce);

         // Protocol
         Paragraph pProt = new Paragraph();
         pProt.setLeading(10f);
         pProt.add(new Chunk("Protocolo de autorização: 314 1300004001 80", FONT_NORMAL));
         cellInfo.addElement(pProt);

         // Autorização
         Paragraph pDataAuth = new Paragraph();
         pDataAuth.setLeading(10f);
         pDataAuth.add(new Chunk("Data de autorização: 10/03/2015 15:03:53", FONT_NORMAL));
         cellInfo.addElement(pDataAuth);

         tableDados.addCell(cellInfo);

         doc.add(tableDados);

         // end
         Paragraph pTrib = new Paragraph("Tributos Totais Incidentes (Lei Federal 12.741/2012): R$65,62", FONT_NORMAL);
         pTrib.setAlignment(Element.ALIGN_CENTER);
         pTrib.setSpacingBefore(5f);
         doc.add(pTrib);
     }

    // helper
    private static void addItemRow(PdfPTable table, String code, String desc, String qty, String un, String valUnit, String valTotal) {
        table.addCell(createCell(code, FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell(desc, FONT_NORMAL, Element.ALIGN_LEFT));
        table.addCell(createCell(qty, FONT_NORMAL, Element.ALIGN_RIGHT));
        table.addCell(createCell(un, FONT_NORMAL, Element.ALIGN_CENTER));
        table.addCell(createCell(valUnit, FONT_NORMAL, Element.ALIGN_RIGHT));
        table.addCell(createCell(valTotal, FONT_NORMAL, Element.ALIGN_RIGHT));
    }

    // helper
    private static PdfPCell createCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER); // Importante: NFC-e limpa geralmente não tem grades visíveis
        cell.setHorizontalAlignment(alignment);
        cell.setPaddingBottom(2f); // Espaçamento pequeno entre linhas
        return cell;
    }

    // helper
    private static void addSeparator(Document doc) throws DocumentException {
        Paragraph p = new Paragraph(" ");
        p.setSpacingAfter(2);
        doc.add(p);
    }
}
