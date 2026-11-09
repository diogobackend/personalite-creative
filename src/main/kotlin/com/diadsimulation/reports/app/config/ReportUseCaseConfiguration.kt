package com.diadsimulation.reports.app.config

import com.diadsimulation.reports.app.adapter.output.adapters.ReportOverviewPersistenceAdapter
import com.diadsimulation.reports.app.adapter.output.adapters.SalesReportPersistenceAdapter
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.input.GetSalesReportInputPort
import com.diadsimulation.reports.core.ports.output.GetReportOverviewOutputPort
import com.diadsimulation.reports.core.ports.output.GetSalesReportOutputPort
import com.diadsimulation.reports.core.usecase.GetReportOverviewUseCaseImplementation
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
}
