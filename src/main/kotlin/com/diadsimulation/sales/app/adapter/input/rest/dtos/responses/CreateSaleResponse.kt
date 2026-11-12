package com.diadsimulation.sales.app.adapter.input.rest.dtos.responses

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class CreateSaleResponse(
    val id: UUID,
    val customerId: UUID,
    val deliveryDeadline: LocalDate?,
    val totalAmount: BigDecimal,
    val downPaymentPercentage: BigDecimal,
    val paidPercentage: BigDecimal,
    val paymentMethod: PaymentMethod,
    val observation: String?,
    val items: List<CreateSaleItemResponse>,
    val deliveredAt: OffsetDateTime? = null,
    val receivedAmount: BigDecimal,
    val remainingAmount: BigDecimal,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
