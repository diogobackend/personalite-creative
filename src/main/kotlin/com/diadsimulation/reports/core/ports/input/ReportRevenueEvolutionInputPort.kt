package com.diadsimulation.reports.core.ports.input

import com.diadsimulation.reports.core.domain.models.ReportRevenueEvolution
import java.time.LocalDate

interface ReportRevenueEvolutionInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportRevenueEvolution
}
