package com.diadsimulation.reports.app.adapter.input.web.dtos.response

import com.diadsimulation.expenses.core.domain.model.ReportCashFlow
import java.math.BigDecimal

data class ReportCashFlowResponse(
    val totalEntries: BigDecimal,
    val totalExits: BigDecimal,
    val balance: BigDecimal,
    val days: List<ReportCashFlowDayResponse>,
) {
    companion object {
        fun from(domain: ReportCashFlow): ReportCashFlowResponse =
            ReportCashFlowResponse(
                totalEntries = domain.totalEntries,
                totalExits = domain.totalExits,
                balance = domain.balance,
                days = domain.days.map(ReportCashFlowDayResponse::from),
            )
    }
}
