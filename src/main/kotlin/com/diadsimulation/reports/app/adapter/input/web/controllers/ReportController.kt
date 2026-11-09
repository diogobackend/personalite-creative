package com.diadsimulation.reports.app.adapter.input.web.controllers

import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportOverviewResponse
import com.diadsimulation.reports.app.adapter.input.web.swagger.ReportApi
import com.diadsimulation.reports.core.domain.models.ReportOverview
import com.diadsimulation.reports.core.ports.input.GetReportOverviewInputPort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/reports")
class ReportController(
    private val getReportOverviewInputPort: GetReportOverviewInputPort,
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
}
