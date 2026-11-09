package com.diadsimulation.reports.app.adapter.input.web.controllers

import com.diadsimulation.expenses.core.ports.input.ReportTransactionsInputPort
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ExpensesReportResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportOverviewResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportTransactionResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportTransactionsResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.SalesReportResponse
import com.diadsimulation.reports.app.adapter.input.web.swagger.ReportApi
import com.diadsimulation.reports.core.domain.models.ReportOverview
import com.diadsimulation.reports.core.ports.input.GetExpensesReportInputPort
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.input.GetSalesReportInputPort
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
}
