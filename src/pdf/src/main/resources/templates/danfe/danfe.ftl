<html>
<head>
    <link rel="stylesheet" href="styles.css"/>
</head>
<body>
    <!-- Header Block -->
    <table class="header-table">
        <tr>
            <td width="40%">
                <div class="box">
                    <span class="bold">${model.emitente.name}</span><br/>
                    ${model.emitente.address}<br/>
                    CNPJ: ${model.emitente.cnpj}
                </div>
            </td>
            <td width="20%" class="center">
                <div class="big-danfe">DANFE</div>
                <div>Documento Auxiliar da<br/>Nota Fiscal Eletrônica</div>
                <div class="operation-type">0 - Entrada<br/>1 - Saída</div>
                <div class="bold box">1</div>
            </td>
            <td width="40%" class="center">
                <!-- io.github.altenhofen.inv.core.pdf.Barcode rendered as image for Phase 1 -->
                <img src="data:image/png;base64,${model.barcodeBase64}" width="280" />
                <div class="access-key">${model.accessKeyFormatted}</div>
            </td>
        </tr>
    </table>

    <!-- ... Natureza da Operação Block ... -->
    <!-- ... Destinatário Block ... -->
    <!-- ... Impostos Block ... -->

    <!-- Itens Table -->
    <table class="items">
        <thead>
            <tr>
                <th>CÓDIGO</th>
                <th>DESCRIÇÃO</th>
                <th>NCM</th>
                <th>CST</th>
                <th>CFOP</th>
                <th>UNID</th>
                <th>QTD</th>
                <th>V.UNIT</th>
                <th>V.TOTAL</th>
            </tr>
        </thead>
        <tbody>
            <#list model.items as item>
            <tr>
                <td>${item.code}</td>
                <td>${item.description}</td>
                <td>${item.ncm}</td>
                <td>${item.cst}</td>
                <td>${item.cfop}</td>
                <td>${item.unit}</td>
                <td class="right">${item.quantity}</td>
                <td class="right">${item.unitPrice}</td>
                <td class="right">${item.totalPrice}</td>
            </tr>
            </#list>
        </tbody>
    </table>
</body>
</html>