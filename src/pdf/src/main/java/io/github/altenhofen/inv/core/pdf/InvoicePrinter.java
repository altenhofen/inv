package io.github.altenhofen.inv.core.pdf;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import io.github.altenhofen.inv.core.domain.viewmodel.DanfeViewModel;
import org.openpdf.pdf.ITextRenderer;
import tools.jackson.databind.json.JsonMapper;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

public class InvoicePrinter {
    public void generatePdf(String jsonInput, String outputPath) throws Exception {
        // 1. Parse JSON to Model
        JsonMapper jsonMapper = new JsonMapper();
        DanfeViewModel model = jsonMapper.readValue(jsonInput, DanfeViewModel.class);

        // 2. Generate Barcodes (augment model)
        String barcode = Barcode.generateCode128(model.accessKey());
        // ... (recreate model with barcode string) ...

        // 3. Process FreeMarker Template
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);
        cfg.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_33).build());
        cfg.setClassForTemplateLoading(this.getClass(), "/templates/danfe/");
        cfg.setDefaultEncoding("UTF-8");
        Template template = cfg.getTemplate("danfe.ftl");

        StringWriter htmlWriter = new StringWriter();
        template.process(Map.of("model", model), htmlWriter);

        // 4. Render HTML to PDF
        String html = htmlWriter.toString();
        try (OutputStream os = new FileOutputStream(outputPath)) {
            ITextRenderer renderer = new ITextRenderer();
            // This line fixes image loading from resources if needed
            renderer.getSharedContext().setBaseURL(
                Objects.requireNonNull(this.getClass().getResource("/templates/")).toExternalForm()
            );
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
        }
    }
}
