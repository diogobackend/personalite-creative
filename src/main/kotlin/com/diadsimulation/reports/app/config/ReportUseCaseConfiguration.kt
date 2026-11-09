package com.diadsimulation.reports.app.config

import com.diadsimulation.reports.app.adapter.output.adapters.ReportOverviewPersistenceAdapter
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.output.GetReportOverviewOutputPort
import com.diadsimulation.reports.core.usecase.GetReportOverviewUseCaseImplementation
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
}
