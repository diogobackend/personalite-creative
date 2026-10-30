package com.diadsimulation.sales.core.domain.model

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class Sale(
    val id: UUID = UUID.randomUUID(),
    val customerId: UUID,
    val items: List<SaleItem>,
    val deliveryDeadline: LocalDate,
    val totalAmount: BigDecimal,
    val downPaymentPercentage: BigDecimal,
    val paidPercentage: BigDecimal,
    val paymentMethod: PaymentMethod,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
