package com.diadsimulation.reports.app.adapter.input.rest.dtos.response

import com.diadsimulation.expenses.core.domain.model.ReportCashFlowDay
import java.math.BigDecimal
import java.time.LocalDate

data class ReportCashFlowDayResponse(
    val date: LocalDate,
    val entries: BigDecimal,
    val exits: BigDecimal,
    val balance: BigDecimal,
) {
    companion object {
        fun from(domain: ReportCashFlowDay): ReportCashFlowDayResponse =
            ReportCashFlowDayResponse(
                date = domain.date,
                entries = domain.entries,
                exits = domain.exits,
                balance = domain.balance,
            )
    }
}
