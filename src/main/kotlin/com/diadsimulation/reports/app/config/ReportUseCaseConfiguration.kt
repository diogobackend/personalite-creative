package com.diadsimulation.reports.app.config

import com.diadsimulation.expenses.core.ports.input.ReportCashFlowInputPort
import com.diadsimulation.expenses.core.ports.input.ReportTransactionsInputPort
import com.diadsimulation.expenses.core.ports.output.GetReportTransactionsOutputPort
import com.diadsimulation.expenses.core.usecase.GetReportCashFlowUseCaseImplementation
import com.diadsimulation.expenses.core.usecase.GetReportTransactionsUseCaseImplementation
import com.diadsimulation.reports.app.adapter.output.adapters.ExpensesReportPersistenceAdapter
import com.diadsimulation.reports.app.adapter.output.adapters.ReportOverviewPersistenceAdapter
import com.diadsimulation.reports.app.adapter.output.adapters.ReportTransactionsPersistenceAdapter
import com.diadsimulation.reports.app.adapter.output.adapters.SalesReportPersistenceAdapter
import com.diadsimulation.reports.core.ports.input.GetExpensesReportInputPort
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.input.GetSalesReportInputPort
import com.diadsimulation.reports.core.ports.input.ReportComparisonInputPort
import com.diadsimulation.reports.core.ports.input.ReportRevenueEvolutionInputPort
import com.diadsimulation.reports.core.ports.output.GetExpensesReportOutputPort
import com.diadsimulation.reports.core.ports.output.GetReportOverviewOutputPort
import com.diadsimulation.reports.core.ports.output.GetSalesReportOutputPort
import com.diadsimulation.reports.core.usecase.GetExpensesReportUseCaseImplementation
import com.diadsimulation.reports.core.usecase.GetReportComparisonUseCaseImplementation
import com.diadsimulation.reports.core.usecase.GetReportOverviewUseCaseImplementation
import com.diadsimulation.reports.core.usecase.GetReportRevenueEvolutionUseCaseImplementation
import com.diadsimulation.reports.core.usecase.GetSalesReportUseCaseImplementation
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReportUseCaseConfiguration {
    @Bean
    fun getReportOverviewOutputPort(entityManager: EntityManager): GetReportOverviewOutputPort =
        ReportOverviewPersistenceAdapter(entityManager)

    @Bean
    fun getReportOverviewInputPort(getReportOverviewOutputPort: GetReportOverviewOutputPort): GetReportOverviewInputPort =
        GetReportOverviewUseCaseImplementation(
            getReportOverviewOutputPort,
        )

    @Bean
    fun getSalesReportOutputPort(entityManager: EntityManager): GetSalesReportOutputPort = SalesReportPersistenceAdapter(entityManager)

    @Bean
    fun getSalesReportInputPort(getSalesReportOutputPort: GetSalesReportOutputPort): GetSalesReportInputPort =
        GetSalesReportUseCaseImplementation(getSalesReportOutputPort)

    @Bean
    fun getExpensesReportOutputPort(entityManager: EntityManager): GetExpensesReportOutputPort =
        ExpensesReportPersistenceAdapter(entityManager)

    @Bean
    fun getExpensesReportInputPort(getExpensesReportOutputPort: GetExpensesReportOutputPort): GetExpensesReportInputPort =
        GetExpensesReportUseCaseImplementation(getExpensesReportOutputPort)

    @Bean
    fun getReportTransactionsOutputPort(entityManager: EntityManager): GetReportTransactionsOutputPort =
        ReportTransactionsPersistenceAdapter(entityManager)

    @Bean
    fun getReportTransactionsInputPort(getReportTransactionsOutputPort: GetReportTransactionsOutputPort): ReportTransactionsInputPort =
        GetReportTransactionsUseCaseImplementation(getReportTransactionsOutputPort)

    @Bean
    fun getReportCashFlowInputPort(reportTransactionsInputPort: ReportTransactionsInputPort): ReportCashFlowInputPort =
        GetReportCashFlowUseCaseImplementation(
            reportTransactionsInputPort = reportTransactionsInputPort,
        )

    @Bean
    fun getReportComparisonInputPort(getReportOverviewInputPort: GetReportOverviewInputPort): ReportComparisonInputPort =
        GetReportComparisonUseCaseImplementation(
            getReportOverviewInputPort = getReportOverviewInputPort,
        )

    @Bean
    fun getReportRevenueEvolutionInputPort(getReportOverviewInputPort: GetReportOverviewInputPort): ReportRevenueEvolutionInputPort =
        GetReportRevenueEvolutionUseCaseImplementation(
            getReportOverviewInputPort = getReportOverviewInputPort,
        )
}
