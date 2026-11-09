package com.diadsimulation.reports.app.adapter.input.web.dtos.response

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class ReportTransactionResponse(
    val id: UUID,
    val type: String,
    val description: String,
    val referenceDate: LocalDate,
    val amount: BigDecimal,
)
