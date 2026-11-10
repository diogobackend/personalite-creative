package com.diadsimulation.reports.app.adapter.input.web.controllers

import com.diadsimulation.expenses.core.ports.input.ReportCashFlowInputPort
import com.diadsimulation.expenses.core.ports.input.ReportTransactionsInputPort
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ExpensesReportResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportCashFlowResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportComparisonResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportOverviewResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportRevenueEvolutionResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportTransactionResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportTransactionsResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.SalesReportResponse
import com.diadsimulation.reports.app.adapter.input.web.swagger.ReportApi
import com.diadsimulation.reports.core.domain.enums.ReportExportFormat
import com.diadsimulation.reports.core.domain.enums.ReportExportFormat.PDF
import com.diadsimulation.reports.core.domain.enums.ReportExportFormat.XLSX
import com.diadsimulation.reports.core.domain.enums.ReportExportType
import com.diadsimulation.reports.core.domain.models.ReportOverview
import com.diadsimulation.reports.core.ports.input.GetExpensesReportInputPort
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.input.GetSalesReportInputPort
import com.diadsimulation.reports.core.ports.input.ReportComparisonInputPort
import com.diadsimulation.reports.core.ports.input.ReportExportInputPort
import com.diadsimulation.reports.core.ports.input.ReportRevenueEvolutionInputPort
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_PDF
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.ZoneId

@RestController
@RequestMapping("/reports")
class ReportController(
    private val getReportOverviewInputPort: GetReportOverviewInputPort,
    private val getSalesReportInputPort: GetSalesReportInputPort,
    private val getExpensesReportInputPort: GetExpensesReportInputPort,
    private val getReportTransactionsInputPort: ReportTransactionsInputPort,
    private val getReportCashFlowInputPort: ReportCashFlowInputPort,
    private val getReportComparisonInputPort: ReportComparisonInputPort,
    private val reportRevenueEvolutionInputPort: ReportRevenueEvolutionInputPort,
    private val reportExportInputPort: ReportExportInputPort,
) : ReportApi {
    @GetMapping("/overview")
    override fun getOverview(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): ReportOverviewResponse {
        val today = LocalDate.now()
        val start = startDate ?: today.withDayOfMonth(1)
        val end = endDate ?: today.withDayOfMonth(today.lengthOfMonth())

        return getReportOverviewInputPort
            .execute(
                startDate = start,
                endDate = end,
            ).toResponse()
    }

    private fun ReportOverview.toResponse() =
        ReportOverviewResponse(
            expectedRevenue = expectedRevenue,
            receivedRevenue = receivedRevenue,
            outstandingRevenue = outstandingRevenue,
            expectedExpenses = expectedExpenses,
            paidExpenses = paidExpenses,
            outstandingExpenses = outstandingExpenses,
            expectedProfit = expectedProfit,
            realizedProfit = realizedProfit,
        )

    @GetMapping("/sales")
    override fun getSalesReport(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): SalesReportResponse {
        val today = LocalDate.now(ZoneId.of("America/Sao_Paulo"))

        val report =
            getSalesReportInputPort.execute(
                startDate = startDate ?: today.withDayOfMonth(1),
                endDate = endDate ?: today,
            )

        return SalesReportResponse(
            totalRevenue = report.totalRevenue,
            totalSales = report.totalSales,
            averageTicket = report.averageTicket,
            receivedAmount = report.receivedAmount,
            outstandingAmount = report.outstandingAmount,
            receivedPercentage = report.receivedPercentage,
        )
    }

    @GetMapping("/expenses")
    override fun getExpensesReport(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): ExpensesReportResponse {
        val today = LocalDate.now(ZoneId.of("America/Sao_Paulo"))

        val report =
            getExpensesReportInputPort.execute(
                startDate = startDate ?: today.withDayOfMonth(1),
                endDate = endDate ?: today,
            )

        return ExpensesReportResponse(
            totalExpenses = report.totalExpenses,
            expectedAmount = report.expectedAmount,
            paidAmount = report.paidAmount,
            outstandingAmount = report.outstandingAmount,
            paidPercentage = report.paidPercentage,
            pendingExpenses = report.pendingExpenses,
        )
    }

    @GetMapping("/transactions")
    override fun getTransactions(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): ReportTransactionsResponse {
        val today = LocalDate.now(ZoneId.of("America/Sao_Paulo"))

        val report =
            getReportTransactionsInputPort.execute(
                startDate = startDate ?: today.withDayOfMonth(1),
                endDate = endDate ?: today,
            )

        return ReportTransactionsResponse(
            totalEntries = report.totalEntries,
            totalExits = report.totalExits,
            balance = report.balance,
            transactions =
                report.transactions.map { transaction ->
                    ReportTransactionResponse(
                        id = transaction.id,
                        type = transaction.type.name,
                        description = transaction.description,
                        referenceDate = transaction.referenceDate,
                        amount = transaction.amount,
                    )
                },
        )
    }

    @GetMapping("/cash-flow")
    override fun getCashFlow(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): ReportCashFlowResponse {
        val today = LocalDate.now(ZoneId.of("America/Sao_Paulo"))

        val report =
            getReportCashFlowInputPort.execute(
                startDate = startDate ?: today.withDayOfMonth(1),
                endDate = endDate ?: today,
            )

        return ReportCashFlowResponse.from(report)
    }

    @GetMapping("/comparison")
    override fun getComparison(
        startDate: LocalDate,
        endDate: LocalDate,
        previousStartDate: LocalDate,
        previousEndDate: LocalDate,
    ): ReportComparisonResponse =
        ReportComparisonResponse.from(
            getReportComparisonInputPort.execute(
                startDate = startDate,
                endDate = endDate,
                previousStartDate = previousStartDate,
                previousEndDate = previousEndDate,
            ),
        )

    @GetMapping("/revenue-evolution")
    override fun getRevenueEvolution(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportRevenueEvolutionResponse =
        ReportRevenueEvolutionResponse.from(
            reportRevenueEvolutionInputPort.execute(
                startDate = startDate,
                endDate = endDate,
            ),
        )

    @GetMapping("/export")
    override fun exportReport(
        startDate: LocalDate,
        endDate: LocalDate,
        format: ReportExportFormat,
        type: ReportExportType,
    ): ResponseEntity<ByteArray> {
        val file =
            reportExportInputPort.execute(
                startDate = startDate,
                endDate = endDate,
                format = format,
                type = type,
            )

        val extension = format.name.lowercase()
        val contentType =
            when (format) {
                PDF -> {
                    APPLICATION_PDF
                }

                XLSX -> {
                    MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    )
                }
            }

        return ResponseEntity
            .ok()
            .contentType(contentType)
            .header(
                CONTENT_DISPOSITION,
                "attachment; filename=\"relatorio-${type.name.lowercase()}.$extension\"",
            ).contentLength(file.size.toLong())
            .body(file)
    }
}
