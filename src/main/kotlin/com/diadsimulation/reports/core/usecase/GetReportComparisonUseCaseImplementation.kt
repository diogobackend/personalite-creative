package com.diadsimulation.reports.core.usecase

import com.diadsimulation.reports.core.domain.models.ReportComparison
import com.diadsimulation.reports.core.domain.models.ReportComparisonPeriod
import com.diadsimulation.reports.core.domain.models.ReportComparisonVariations
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import com.diadsimulation.reports.core.ports.input.ReportComparisonInputPort
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate

class GetReportComparisonUseCaseImplementation(
    private val getReportOverviewInputPort: GetReportOverviewInputPort,
) : ReportComparisonInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
        previousStartDate: LocalDate,
        previousEndDate: LocalDate,
    ): ReportComparison {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        require(!previousStartDate.isAfter(previousEndDate)) {
            "A data inicial do período anterior não pode ser posterior à data final."
        }

        val current =
            getReportOverviewInputPort.execute(
                startDate = startDate,
                endDate = endDate,
            )

        val previous =
            getReportOverviewInputPort.execute(
                startDate = previousStartDate,
                endDate = previousEndDate,
            )

        return ReportComparison(
            currentPeriod =
                ReportComparisonPeriod(
                    startDate = startDate,
                    endDate = endDate,
                    revenue = current.expectedRevenue,
                    expenses = current.expectedExpenses,
                    profit = current.expectedProfit,
                ),
            previousPeriod =
                ReportComparisonPeriod(
                    startDate = previousStartDate,
                    endDate = previousEndDate,
                    revenue = previous.expectedRevenue,
                    expenses = previous.expectedExpenses,
                    profit = previous.expectedProfit,
                ),
            variations =
                ReportComparisonVariations(
                    revenuePercentage =
                        calculateVariation(
                            current.expectedRevenue,
                            previous.expectedRevenue,
                        ),
                    expensesPercentage =
                        calculateVariation(
                            current.expectedExpenses,
                            previous.expectedExpenses,
                        ),
                    profitPercentage =
                        calculateVariation(
                            current.expectedProfit,
                            previous.expectedProfit,
                        ),
                ),
        )
    }

    private fun calculateVariation(
        current: BigDecimal,
        previous: BigDecimal,
    ): BigDecimal? {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return null
        }

        return current
            .subtract(previous)
            .multiply(BigDecimal.valueOf(100))
            .divide(previous.abs(), 2, HALF_UP)
    }
}
