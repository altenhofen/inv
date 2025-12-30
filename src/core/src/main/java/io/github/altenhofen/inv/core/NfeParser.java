package io.github.altenhofen.inv.core;

import io.github.altenhofen.inv.core.domain.models.Item;
import io.github.altenhofen.inv.core.domain.viewmodel.DanfeViewModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

public class NfeParser {

    public DanfeViewModel parse(InputStream xmlStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser saxParser = factory.newSAXParser();

        NfeHandler handler = new NfeHandler();
        saxParser.parse(xmlStream, handler);

        return handler.getResult();
    }

    private static class NfeHandler extends DefaultHandler {

        // State management
        private DanfeViewModel.Builder builder = new DanfeViewModel.Builder();
        private StringBuilder currentValue = new StringBuilder();
        private Deque<String> path = new ArrayDeque();

        // Item handling
        private Item.Builder currentItem;

        public DanfeViewModel getResult() {
            return builder.build();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            path.push(qName);
            currentValue.setLength(0); // Clear buffer

            if ("det".equals(qName)) {
                currentItem = new Item.Builder();
                // "nItem" attribute is often useful
                String nItem = attributes.getValue("nItem");
            }

            // Capture Access Key from the ID attribute
            if ("infNFe".equals(qName)) {
                String id = attributes.getValue("Id");
                if (id != null && id.startsWith("NFe")) {
                    builder.accessKey(id.substring(3)); // Strip "NFe" prefix
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String value = currentValue.toString().trim();

            // Routing based on the current path in the XML tree
            // This is the "manual" mapping. It's fast because it's just string comparisons.

            if (inPath("NFe", "infNFe", "ide", localName)) {
                switch (localName) {
                    case "natOp" -> builder.natureOfOperation(value);
                    case "cNF" -> builder.protocol(value); // Careful mapping here
                    case "mod" -> builder.model(value);
                    case "serie" -> builder.series(value);
                    case "nNF" -> builder.nfNumber(value);
                    case "dEmi" -> builder.emissionDate(value);
                    case "dSaiEnt" -> builder.ioDate(value);
                    case "tpNF" -> builder.ioType(value);
                    case "tpAmb" -> builder.isHomolog("2".equals(value));
                    case "finNFe" -> builder.finality(value);
                }
            }

            if (inPath("NFe", "infNFe", "emit")) {
                switch (qName) {
                    case "xNome" -> builder.emitenteName(value);
                    case "CNPJ" -> builder.emitenteCnpj(value);
                }
            }

            if (inPath("NFe", "infNFe", "emit", "enderEmit")) {
                // Build address manually or use helper
            }

            if (inPath("NFe", "infNFe", "det", "prod", localName)) {
                switch (qName) {
                    case "cProd" -> currentItem.code(value);
                    case "xProd" -> currentItem.description(value);
                    case "NCM" -> currentItem.ncm(value);
                    case "CFOP" -> currentItem.cfop(value);
                    case "uCom" -> currentItem.unit(value);
                    case "qCom" -> currentItem.quantity(new BigDecimal(value));
                    case "vUnCom" -> currentItem.unitPrice(new BigDecimal(value));
                    case "vProd" -> currentItem.totalPrice(new BigDecimal(value));
                }
            }

            // Finish Item
            if ("det".equals(qName)) {
                builder.addItem(currentItem.build());
                currentItem = null;
            }

            path.pop();
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            currentValue.append(ch, start, length);
        }

        // poor man's xpath
        private boolean inPath(String... expectedPath) {
            if (path.size() < expectedPath.length) return false;

            Iterator<String> it = path.iterator();

            for (int i = expectedPath.length - 1; i >= 0; i--) {
                if (!it.hasNext()) return false;

                String current = it.next();
                String expected = expectedPath[i];

                if (!current.equals(expected)) {
                    return false;
                }
            }
            return true;
        }
    }
}