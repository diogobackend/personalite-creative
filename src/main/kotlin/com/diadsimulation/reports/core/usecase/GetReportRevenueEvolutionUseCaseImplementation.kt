package com.diadsimulation.reports.core.usecase

import com.diadsimulation.reports.core.domain.models.ReportRevenueEvolution
import com.diadsimulation.reports.core.domain.models.ReportRevenueEvolutionMonth
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.input.ReportRevenueEvolutionInputPort
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.time.YearMonth

class GetReportRevenueEvolutionUseCaseImplementation(
    private val getReportOverviewInputPort: GetReportOverviewInputPort,
) : ReportRevenueEvolutionInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
    ): ReportRevenueEvolution {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        val months =
            generateSequence(YearMonth.from(startDate)) {
                it.plusMonths(1)
            }.takeWhile { !it.isAfter(YearMonth.from(endDate)) }
                .map { month ->
                    val monthStart =
                        maxOf(
                            month.atDay(1),
                            startDate,
                        )

                    val monthEnd =
                        minOf(
                            month.atEndOfMonth(),
                            endDate,
                        )

                    val overview =
                        getReportOverviewInputPort.execute(
                            startDate = monthStart,
                            endDate = monthEnd,
                        )

                    ReportRevenueEvolutionMonth(
                        month = month,
                        revenue = overview.receivedRevenue,
                    )
                }.toList()

        return ReportRevenueEvolution(
            totalRevenue =
                months.fold(ZERO) { total, month ->
                    total.add(month.revenue)
                },
            months = months,
        )
    }
}
