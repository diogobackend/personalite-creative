package com.diadsimulation.sales.core.domain.model

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class Sale(
    val id: UUID = UUID.randomUUID(),
    val customerId: UUID,
    val items: List<SaleItem>,
    val deliveryDeadline: LocalDate? = null,
    val totalAmount: BigDecimal,
    val downPaymentPercentage: BigDecimal,
    val paidPercentage: BigDecimal,
    val paymentMethod: PaymentMethod,
    val observation: String? = null,
    val deliveredAt: OffsetDateTime? = null,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    val receivedAmount: BigDecimal
        get() =
            totalAmount
                .multiply(paidPercentage)
                .divide(HUNDRED, 2, HALF_UP)

    val remainingAmount: BigDecimal
        get() = totalAmount.subtract(receivedAmount)

    private companion object {
        val HUNDRED = BigDecimal("100")
    }
}
