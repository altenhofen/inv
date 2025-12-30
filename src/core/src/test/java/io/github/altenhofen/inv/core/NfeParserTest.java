package io.github.altenhofen.inv.core;

import io.github.altenhofen.inv.core.domain.viewmodel.DanfeViewModel;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamReader;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class NfeParserTest {
    // xml exemplo retirado de https://www.webdanfe.com.br/danfe/ExemploXml.aspx
    @Test
    void parse() throws Exception {
        InputStream xmlStream = NfeParserTest.class.getResourceAsStream("/sample.xml");

        assertNotNull(xmlStream);
        NfeParser nfeParser = new NfeParser();
        DanfeViewModel vm = nfeParser.parse(xmlStream);

        assertNotNull(vm);
        assertNotNull(vm.ide());
    }
}