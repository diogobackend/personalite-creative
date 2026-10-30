package com.diadsimulation.sales.app.adapter.input.web.dtos.responses

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus
import com.diadsimulation.sales.core.domain.enums.SalePaymentStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class SaleSummaryResponse(
    val id: UUID,
    val customerId: UUID,
    val deliveryDeadline: LocalDate?,
    val totalAmount: BigDecimal,
    val downPaymentPercentage: BigDecimal,
    val downPaymentAmount: BigDecimal,
    val paidPercentage: BigDecimal,
    val paidAmount: BigDecimal,
    val remainingPercentage: BigDecimal,
    val remainingAmount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val paymentStatus: SalePaymentStatus,
    val deadlineStatus: SaleDeadlineStatus,
    val observation: String?,
    val items: List<SaleItemSummaryResponse>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
