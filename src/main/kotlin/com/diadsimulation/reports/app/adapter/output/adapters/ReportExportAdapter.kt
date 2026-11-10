
package com.diadsimulation.reports.app.adapter.output.adapters

import com.diadsimulation.expenses.core.ports.input.ReportCashFlowInputPort
import com.diadsimulation.expenses.core.ports.input.ReportTransactionsInputPort
import com.diadsimulation.reports.core.domain.enums.FieldType
import com.diadsimulation.reports.core.domain.enums.FieldType.CURRENCY
import com.diadsimulation.reports.core.domain.enums.FieldType.DATE
import com.diadsimulation.reports.core.domain.enums.FieldType.MONTH
import com.diadsimulation.reports.core.domain.enums.FieldType.NUMBER
import com.diadsimulation.reports.core.domain.enums.FieldType.PERCENTAGE
import com.diadsimulation.reports.core.domain.enums.FieldType.TEXT
import com.diadsimulation.reports.core.domain.enums.ReportExportFormat
import com.diadsimulation.reports.core.domain.enums.ReportExportFormat.PDF
import com.diadsimulation.reports.core.domain.enums.ReportExportFormat.XLSX
import com.diadsimulation.reports.core.domain.enums.ReportExportType
import com.diadsimulation.reports.core.domain.enums.ReportExportType.ALL
import com.diadsimulation.reports.core.domain.enums.ReportExportType.CASH_FLOW
import com.diadsimulation.reports.core.domain.enums.ReportExportType.COMPARISON
import com.diadsimulation.reports.core.domain.enums.ReportExportType.EXPENSES
import com.diadsimulation.reports.core.domain.enums.ReportExportType.OVERVIEW
import com.diadsimulation.reports.core.domain.enums.ReportExportType.REVENUE_EVOLUTION
import com.diadsimulation.reports.core.domain.enums.ReportExportType.SALES
import com.diadsimulation.reports.core.domain.enums.ReportExportType.TRANSACTIONS
import com.diadsimulation.reports.core.ports.input.GetExpensesReportInputPort
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.input.GetSalesReportInputPort
import com.diadsimulation.reports.core.ports.input.ReportComparisonInputPort
import com.diadsimulation.reports.core.ports.input.ReportRevenueEvolutionInputPort
import com.diadsimulation.reports.core.ports.output.ReportExportOutputPort
import com.lowagie.text.Document
import com.lowagie.text.Element.ALIGN_MIDDLE
import com.lowagie.text.FontFactory
import com.lowagie.text.FontFactory.HELVETICA
import com.lowagie.text.FontFactory.HELVETICA_BOLD
import com.lowagie.text.PageSize.A4
import com.lowagie.text.Paragraph
import com.lowagie.text.Phrase
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND
import org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.beans.Introspector
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.TemporalAccessor
import java.util.LinkedHashMap
import java.util.Locale
import java.util.UUID

class ReportExportAdapter(
    private val getReportOverviewInputPort: GetReportOverviewInputPort,
    private val getSalesReportInputPort: GetSalesReportInputPort,
    private val getExpensesReportInputPort: GetExpensesReportInputPort,
    private val reportTransactionsInputPort: ReportTransactionsInputPort,
    private val reportCashFlowInputPort: ReportCashFlowInputPort,
    private val reportComparisonInputPort: ReportComparisonInputPort,
    private val reportRevenueEvolutionInputPort: ReportRevenueEvolutionInputPort,
) : ReportExportOutputPort {
    override fun export(
        startDate: LocalDate,
        endDate: LocalDate,
        format: ReportExportFormat,
        type: ReportExportType,
    ): ByteArray {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        val sections = buildSections(startDate, endDate, type)

        return when (format) {
            PDF -> exportPdf(sections, startDate, endDate)
            XLSX -> exportExcel(sections, startDate, endDate)
        }
    }

    private fun buildSections(
        startDate: LocalDate,
        endDate: LocalDate,
        type: ReportExportType,
    ): List<ReportSection> =
        buildList {
            if (type == ALL || type == OVERVIEW) {
                add(
                    createSection(
                        "Visão geral",
                        getReportOverviewInputPort.execute(startDate, endDate),
                    ),
                )
            }

            if (type == ALL || type == SALES) {
                add(
                    createSection(
                        "Vendas",
                        getSalesReportInputPort.execute(startDate, endDate),
                    ),
                )
            }

            if (type == ALL || type == EXPENSES) {
                add(
                    createSection(
                        "Despesas",
                        getExpensesReportInputPort.execute(startDate, endDate),
                    ),
                )
            }

            if (type == ALL || type == TRANSACTIONS) {
                add(
                    createSection(
                        "Movimentações",
                        reportTransactionsInputPort.execute(startDate, endDate),
                    ),
                )
            }

            if (type == ALL || type == CASH_FLOW) {
                add(
                    createSection(
                        "Fluxo de caixa",
                        reportCashFlowInputPort.execute(startDate, endDate),
                    ),
                )
            }

            if (type == ALL || type == COMPARISON) {
                val days = DAYS.between(startDate, endDate) + 1
                val previousEndDate = startDate.minusDays(1)
                val previousStartDate = previousEndDate.minusDays(days - 1)

                add(
                    createSection(
                        "Comparativo",
                        reportComparisonInputPort.execute(
                            startDate,
                            endDate,
                            previousStartDate,
                            previousEndDate,
                        ),
                    ),
                )
            }

            if (type == ALL || type == REVENUE_EVOLUTION) {
                add(
                    createSection(
                        "Evolução do faturamento",
                        reportRevenueEvolutionInputPort.execute(startDate, endDate),
                    ),
                )
            }
        }

    private fun createSection(
        title: String,
        data: Any,
    ): ReportSection {
        val properties = readProperties(data)

        val summary =
            properties
                .filterValues { it !is Collection<*> && it !is Array<*> }
                .map { (name, value) ->
                    ReportField(
                        label = formatLabel(name),
                        value = value,
                        type = resolveFieldType(name, value),
                    )
                }

        val tables =
            properties
                .filterValues { it is Collection<*> || it is Array<*> }
                .map { (name, value) ->
                    val items =
                        when (value) {
                            is Collection<*> -> value.toList()
                            is Array<*> -> value.toList()
                            else -> emptyList()
                        }

                    createTable(formatLabel(name), items)
                }

        return ReportSection(
            title = title,
            summary = summary,
            tables = tables,
        )
    }

    private fun createTable(
        title: String,
        items: List<Any?>,
    ): ReportTable {
        val records =
            items
                .filterNotNull()
                .map { item ->
                    if (isSimpleValue(item)) {
                        linkedMapOf("value" to item)
                    } else {
                        readProperties(item)
                    }
                }

        val columns =
            records
                .flatMap { it.keys }
                .distinct()

        val headers = columns.map(::formatLabel)

        val rows =
            records.map { record ->
                columns.map { column ->
                    val value = record[column]

                    ReportField(
                        label = formatLabel(column),
                        value = value,
                        type = resolveFieldType(column, value),
                    )
                }
            }

        return ReportTable(
            title = title,
            headers = headers,
            rows = rows,
        )
    }

    private fun readProperties(value: Any): LinkedHashMap<String, Any?> {
        val properties = linkedMapOf<String, Any?>()

        Introspector
            .getBeanInfo(value.javaClass, Any::class.java)
            .propertyDescriptors
            .filter { it.readMethod != null }
            .filterNot { it.name == "class" }
            .forEach { descriptor ->
                val propertyValue = descriptor.readMethod.invoke(value)

                if (
                    propertyValue != null &&
                    !isSimpleValue(propertyValue) &&
                    propertyValue !is Collection<*> &&
                    propertyValue !is Array<*>
                ) {
                    readProperties(propertyValue).forEach { (name, nestedValue) ->
                        properties["${descriptor.name}.$name"] = nestedValue
                    }
                } else {
                    properties[descriptor.name] = propertyValue
                }
            }

        return properties
    }

    private fun isSimpleValue(value: Any): Boolean =
        value is String ||
            value is Number ||
            value is Boolean ||
            value is Enum<*> ||
            value is LocalDate ||
            value is YearMonth ||
            value is UUID ||
            value is TemporalAccessor

    private fun resolveFieldType(
        name: String,
        value: Any?,
    ): FieldType {
        val fieldName = name.substringAfterLast(".")

        return when {
            fieldName.endsWith("Percentage", ignoreCase = true) -> {
                PERCENTAGE
            }

            value == null -> {
                TEXT
            }

            value is BigDecimal -> {
                CURRENCY
            }

            value is LocalDate -> {
                DATE
            }

            value is YearMonth -> {
                MONTH
            }

            value is Number -> {
                NUMBER
            }

            else -> {
                TEXT
            }
        }
    }

    private fun formatLabel(value: String): String {
        val labels =
            mapOf(
                "expectedExpenses" to "Despesas previstas",
                "expectedProfit" to "Lucro líquido previsto",
                "expectedRevenue" to "Faturamento bruto",
                "outstandingExpenses" to "Despesas pendentes",
                "outstandingRevenue" to "Valores a receber",
                "paidExpenses" to "Despesas pagas",
                "realizedProfit" to "Lucro líquido realizado",
                "receivedRevenue" to "Dinheiro recebido",
                "totalRevenue" to "Faturamento total",
                "totalSales" to "Quantidade de vendas",
                "averageTicket" to "Ticket médio",
                "receivedAmount" to "Valor recebido",
                "outstandingAmount" to "Valor pendente",
                "receivedPercentage" to "Percentual recebido",
                "totalExpenses" to "Quantidade de despesas",
                "expectedAmount" to "Valor previsto",
                "paidAmount" to "Valor pago",
                "paidPercentage" to "Percentual pago",
                "pendingExpenses" to "Despesas pendentes (quantidade)",
                "totalEntries" to "Total de entradas",
                "totalExits" to "Total de saídas",
                "balance" to "Saldo",
                "transactions" to "Transações",
                "days" to "Movimentação diária",
                "months" to "Evolução mensal",
                "currentPeriod" to "Período atual",
                "previousPeriod" to "Período anterior",
                "variations" to "Variações",
                "startDate" to "Data inicial",
                "endDate" to "Data final",
                "revenue" to "Faturamento",
                "expenses" to "Despesas",
                "profit" to "Lucro",
                "revenuePercentage" to "Variação do faturamento",
                "expensesPercentage" to "Variação das despesas",
                "profitPercentage" to "Variação do lucro",
                "id" to "Identificador",
                "type" to "Tipo",
                "description" to "Descrição",
                "referenceDate" to "Data de referência",
                "amount" to "Valor",
                "date" to "Data",
                "entries" to "Entradas",
                "exits" to "Saídas",
                "month" to "Mês",
                "value" to "Valor",
                "ENTRY" to "Entrada",
                "EXIT" to "Saída",
            )
        return value.split(".").joinToString(" - ") { part ->
            labels[part] ?: part
                .replace(Regex("([a-z])([A-Z])"), "$1 $2")
                .replace("_", " ")
                .lowercase(BR_LOCALE)
                .replaceFirstChar { it.titlecase(BR_LOCALE) }
        }
    }

    private fun formatValue(field: ReportField): String {
        val value = field.value ?: return "-"

        return when (field.type) {
            CURRENCY -> {
                currencyFormatter().format(value)
            }

            PERCENTAGE -> {
                "${numberFormatter().format(value)}%"
            }

            NUMBER -> {
                numberFormatter().format(value)
            }

            DATE -> {
                (value as LocalDate).format(DATE_FORMAT)
            }

            MONTH -> {
                (value as YearMonth).format(MONTH_FORMAT)
            }

            TEXT -> {
                when (value) {
                    is Enum<*> -> formatLabel(value.name)
                    is Boolean -> if (value) "Sim" else "Não"
                    else -> value.toString()
                }
            }
        }
    }

    private fun currencyFormatter(): NumberFormat =
        NumberFormat.getCurrencyInstance(BR_LOCALE).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }

    private fun numberFormatter(): NumberFormat =
        NumberFormat.getNumberInstance(BR_LOCALE).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 2
        }

    private fun exportPdf(
        sections: List<ReportSection>,
        startDate: LocalDate,
        endDate: LocalDate,
    ): ByteArray {
        val output = ByteArrayOutputStream()
        val document =
            Document(
                A4.rotate(),
                30f,
                30f,
                40f,
                40f,
            )

        PdfWriter.getInstance(document, output)
        document.open()

        try {
            val titleFont =
                FontFactory.getFont(
                    HELVETICA_BOLD,
                    16f,
                )

            val sectionFont =
                FontFactory.getFont(
                    HELVETICA_BOLD,
                    12f,
                )

            val normalFont =
                FontFactory.getFont(
                    HELVETICA,
                    9f,
                )

            sections.forEachIndexed { index, section ->
                if (index > 0) {
                    document.newPage()
                }

                val header =
                    PdfPTable(2).apply {
                        widthPercentage = 100f
                        setWidths(floatArrayOf(1f, 5f))
                        setSpacingAfter(18f)
                    }
                val logoCell =
                    PdfPCell().apply {
                        border = 0
                        setPadding(4f)
                    }
                loadLogo()?.let { bytes ->
                    val logo =
                        com.lowagie.text.Image
                            .getInstance(bytes)
                    logo.scaleToFit(85f, 55f)
                    logoCell.addElement(logo)
                }
                header.addCell(logoCell)
                header.addCell(
                    PdfPCell().apply {
                        border = 0
                        verticalAlignment = ALIGN_MIDDLE
                        addElement(Paragraph("PERSONALITÉ CREATIVE", titleFont))
                        addElement(Paragraph("Relatórios financeiros", normalFont))
                    },
                )
                document.add(header)

                document.add(
                    Paragraph(
                        "Período: ${startDate.format(DATE_FORMAT)} a ${endDate.format(DATE_FORMAT)}",
                        normalFont,
                    ),
                )

                document.add(Paragraph(" "))

                document.add(
                    Paragraph(section.title, sectionFont).apply {
                        font.color = GOLD_COLOR
                    },
                )

                document.add(Paragraph(" "))

                if (section.summary.isNotEmpty()) {
                    val summaryTable =
                        PdfPTable(2).apply {
                            widthPercentage = 100f
                            setWidths(floatArrayOf(2f, 3f))
                            setSpacingAfter(15f)
                        }

                    section.summary.forEach { field ->
                        summaryTable.addCell(
                            pdfCell(field.label, true),
                        )

                        summaryTable.addCell(
                            pdfCell(formatValue(field), false),
                        )
                    }

                    document.add(summaryTable)
                }

                section.tables.forEach { table ->
                    document.add(
                        Paragraph(table.title, sectionFont),
                    )

                    document.add(Paragraph(" "))

                    if (table.headers.isEmpty()) {
                        document.add(
                            Paragraph(
                                "Nenhum registro encontrado.",
                                normalFont,
                            ),
                        )
                    } else {
                        val pdfTable =
                            PdfPTable(table.headers.size).apply {
                                widthPercentage = 100f
                                headerRows = 1
                                setSpacingAfter(15f)
                            }

                        table.headers.forEach { header ->
                            pdfTable.addCell(
                                pdfCell(header, true),
                            )
                        }

                        table.rows.forEach { row ->
                            row.forEach { field ->
                                pdfTable.addCell(
                                    pdfCell(formatValue(field), false),
                                )
                            }
                        }

                        document.add(pdfTable)
                    }

                    document.add(Paragraph(" "))
                }
                document.add(Paragraph("Personalité Creative | Página ${index + 1} de ${sections.size}", normalFont))
            }
        } finally {
            document.close()
        }

        return output.toByteArray()
    }

    private fun pdfCell(
        value: String,
        header: Boolean,
    ): PdfPCell {
        val font =
            FontFactory
                .getFont(
                    if (header) HELVETICA_BOLD else HELVETICA,
                    8f,
                ).apply {
                    color = if (header) java.awt.Color.WHITE else DARK_COLOR
                }

        return PdfPCell(Phrase(value, font)).apply {
            setPadding(6f)
            verticalAlignment = ALIGN_MIDDLE

            if (header) {
                backgroundColor = GOLD_COLOR
            }
        }
    }

    private fun exportExcel(
        sections: List<ReportSection>,
        startDate: LocalDate,
        endDate: LocalDate,
    ): ByteArray {
        val output = ByteArrayOutputStream()

        XSSFWorkbook().use { workbook ->
            val headerStyle =
                workbook.createCellStyle().apply {
                    fillForegroundColor = org.apache.poi.ss.usermodel.IndexedColors.BROWN.index
                    fillPattern = SOLID_FOREGROUND
                    alignment = LEFT

                    setFont(
                        workbook.createFont().apply {
                            bold = true
                            color = org.apache.poi.ss.usermodel.IndexedColors.WHITE.index
                        },
                    )
                }

            val titleStyle =
                workbook.createCellStyle().apply {
                    setFont(
                        workbook.createFont().apply {
                            bold = true
                            fontHeightInPoints = 14
                            color = org.apache.poi.ss.usermodel.IndexedColors.BROWN.index
                        },
                    )
                }

            val currencyStyle =
                workbook.createCellStyle().apply {
                    dataFormat =
                        workbook
                            .createDataFormat()
                            .getFormat("\"R$\" #,##0.00")
                }

            val percentageStyle =
                workbook.createCellStyle().apply {
                    dataFormat =
                        workbook
                            .createDataFormat()
                            .getFormat("0.00\"%\"")
                }

            val numberStyle =
                workbook.createCellStyle().apply {
                    dataFormat =
                        workbook
                            .createDataFormat()
                            .getFormat("#,##0.##")
                }

            val dateStyle =
                workbook.createCellStyle().apply {
                    dataFormat =
                        workbook
                            .createDataFormat()
                            .getFormat("dd/mm/yyyy")
                }

            val monthStyle =
                workbook.createCellStyle().apply {
                    dataFormat =
                        workbook
                            .createDataFormat()
                            .getFormat("mm/yyyy")
                }

            fun writeCell(
                cell: org.apache.poi.ss.usermodel.Cell,
                field: ReportField,
            ) {
                val value = field.value

                if (value == null) {
                    cell.setCellValue("-")
                    return
                }

                when (field.type) {
                    CURRENCY -> {
                        cell.setCellValue((value as Number).toDouble())
                        cell.cellStyle = currencyStyle
                    }

                    PERCENTAGE -> {
                        cell.setCellValue((value as Number).toDouble())
                        cell.cellStyle = percentageStyle
                    }

                    NUMBER -> {
                        cell.setCellValue((value as Number).toDouble())
                        cell.cellStyle = numberStyle
                    }

                    DATE -> {
                        cell.setCellValue(value as LocalDate)
                        cell.cellStyle = dateStyle
                    }

                    MONTH -> {
                        cell.setCellValue((value as YearMonth).atDay(1))
                        cell.cellStyle = monthStyle
                    }

                    TEXT -> {
                        cell.setCellValue(formatValue(field))
                    }
                }
            }

            sections.forEach { section ->
                val sheet =
                    workbook.createSheet(
                        section.title.take(31),
                    )

                var rowIndex = 0
                loadLogo()?.let { bytes ->
                    val pictureIndex = workbook.addPicture(bytes, org.apache.poi.ss.usermodel.Workbook.PICTURE_TYPE_PNG)
                    val drawing = sheet.createDrawingPatriarch()
                    val anchor =
                        workbook.creationHelper.createClientAnchor().apply {
                            setCol1(0)
                            row1 = 0
                            setCol2(2)
                            setRow2(4)
                        }
                    drawing.createPicture(anchor, pictureIndex)
                    rowIndex = 5
                }

                sheet
                    .createRow(rowIndex++)
                    .createCell(0)
                    .apply {
                        setCellValue("PERSONALITÉ CREATIVE | ${section.title}")
                        setCellStyle(titleStyle)
                    }

                sheet
                    .createRow(rowIndex++)
                    .createCell(0)
                    .setCellValue(
                        "Período: ${startDate.format(DATE_FORMAT)} a ${endDate.format(DATE_FORMAT)}",
                    )

                rowIndex++

                section.summary.forEach { field ->
                    val row = sheet.createRow(rowIndex++)

                    row.createCell(0).apply {
                        setCellValue(field.label)
                        setCellStyle(headerStyle)
                    }

                    writeCell(
                        row.createCell(1),
                        field,
                    )
                }

                rowIndex += 2

                section.tables.forEach { table ->
                    sheet
                        .createRow(rowIndex++)
                        .createCell(0)
                        .apply {
                            setCellValue(table.title)
                            setCellStyle(titleStyle)
                        }

                    if (table.headers.isEmpty()) {
                        sheet
                            .createRow(rowIndex++)
                            .createCell(0)
                            .setCellValue("Nenhum registro encontrado.")
                    } else {
                        val headerRow = sheet.createRow(rowIndex++)

                        table.headers.forEachIndexed { columnIndex, header ->
                            headerRow.createCell(columnIndex).apply {
                                setCellValue(header)
                                setCellStyle(headerStyle)
                            }
                        }

                        table.rows.forEach { fields ->
                            val row = sheet.createRow(rowIndex++)

                            fields.forEachIndexed { columnIndex, field ->
                                writeCell(
                                    row.createCell(columnIndex),
                                    field,
                                )
                            }
                        }

                        if (table.rows.isEmpty()) {
                            sheet
                                .createRow(rowIndex++)
                                .createCell(0)
                                .setCellValue("Nenhum registro encontrado.")
                        }
                    }

                    rowIndex += 2
                }

                val columnCount =
                    maxOf(
                        2,
                        section.tables.maxOfOrNull { it.headers.size } ?: 0,
                    )

                repeat(columnCount) { column ->
                    sheet.setColumnWidth(column, 6500)
                }
                sheet
                    .createRow(rowIndex + 1)
                    .createCell(0)
                    .setCellValue("Personalité Creative | Relatório financeiro")
            }

            workbook.write(output)
        }

        return output.toByteArray()
    }

    private data class ReportField(
        val label: String,
        val value: Any?,
        val type: FieldType,
    )

    private data class ReportSection(
        val title: String,
        val summary: List<ReportField>,
        val tables: List<ReportTable>,
    )

    private data class ReportTable(
        val title: String,
        val headers: List<String>,
        val rows: List<List<ReportField>>,
    )

    private fun loadLogo(): ByteArray? = javaClass.getResourceAsStream("/reports/personalite-logo.png")?.use { it.readBytes() }

    companion object {
        private val GOLD_COLOR = java.awt.Color(190, 145, 65)
        private val DARK_COLOR = java.awt.Color(53, 47, 42)
        private val BR_LOCALE = Locale.forLanguageTag("pt-BR")
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        private val MONTH_FORMAT = DateTimeFormatter.ofPattern("MM/yyyy")
    }
}
