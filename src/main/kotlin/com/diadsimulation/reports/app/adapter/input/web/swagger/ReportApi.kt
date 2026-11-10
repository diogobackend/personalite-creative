package com.diadsimulation.reports.app.adapter.input.web.swagger

import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ExpensesReportResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportCashFlowResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportComparisonResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportOverviewResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportRevenueEvolutionResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportTransactionsResponse
import com.diadsimulation.reports.app.adapter.input.web.dtos.response.SalesReportResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Tag(name = "Relatórios", description = "Relatórios financeiros")
interface ReportApi {
    @Operation(
        summary = "Consultar visão geral financeira",
        description = "Retorna os indicadores financeiros do período informado. Por padrão, utiliza o mês atual.",
    )
    fun getOverview(
        @Parameter(description = "Data inicial do período (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate?,
        @Parameter(description = "Data final do período (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate?,
    ): ReportOverviewResponse

    @Operation(
        summary = "Consultar relatório de vendas",
        description = "Retorna os indicadores consolidados de vendas no período informado.",
    )
    fun getSalesReport(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate?,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate?,
    ): SalesReportResponse

    @Operation(
        summary = "Consultar relatório de despesas",
        description = "Retorna os indicadores de despesas no período informado.",
    )
    fun getExpensesReport(
        @Parameter(description = "Data inicial (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate?,
        @Parameter(description = "Data final (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate?,
    ): ExpensesReportResponse

    @Operation(
        summary = "Consultar transações financeiras",
        description = "Retorna as entradas, saídas e o saldo financeiro do período informado. Por padrão, utiliza o mês atual até hoje.",
    )
    fun getTransactions(
        @Parameter(description = "Data inicial (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate?,
        @Parameter(description = "Data final (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate?,
    ): ReportTransactionsResponse

    @Operation(
        summary = "Consultar fluxo de caixa",
        description = "Retorna entradas, saídas e saldo agrupados por data de referência no período informado.",
    )
    fun getCashFlow(
        @Parameter(description = "Data inicial (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate?,
        @Parameter(description = "Data final (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate?,
    ): ReportCashFlowResponse

    @Operation(
        summary = "Comparar períodos financeiros",
        description = "Compara faturamento, despesas e lucro entre dois períodos, retornando suas variações percentuais.",
    )
    fun getComparison(
        @Parameter(description = "Data inicial do período atual (yyyy-MM-dd)")
        @RequestParam
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate,
        @Parameter(description = "Data final do período atual (yyyy-MM-dd)")
        @RequestParam
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate,
        @Parameter(description = "Data inicial do período anterior (yyyy-MM-dd)")
        @RequestParam
        @DateTimeFormat(iso = DATE)
        previousStartDate: LocalDate,
        @Parameter(description = "Data final do período anterior (yyyy-MM-dd)")
        @RequestParam
        @DateTimeFormat(iso = DATE)
        previousEndDate: LocalDate,
    ): ReportComparisonResponse

    @Operation(
        summary = "Consultar evolução do faturamento",
        description = "Retorna o faturamento total e sua evolução mensal no período informado.",
    )
    fun getRevenueEvolution(
        @Parameter(description = "Data inicial do período (yyyy-MM-dd)")
        @RequestParam
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate,
        @Parameter(description = "Data final do período (yyyy-MM-dd)")
        @RequestParam
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate,
    ): ReportRevenueEvolutionResponse
}
