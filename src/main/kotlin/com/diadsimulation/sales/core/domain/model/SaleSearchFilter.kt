package com.diadsimulation.sales.core.domain.model

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus
import com.diadsimulation.sales.core.domain.enums.SalePaymentStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class SaleSearchFilter(
    val search: String? = null,
    val saleId: UUID? = null,
    val customer: String? = null,
    val productName: String? = null,
    val productCommercialCode: String? = null,
    val color: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val size: String? = null,
    val material: String? = null,
    val minTotalAmount: BigDecimal? = null,
    val maxTotalAmount: BigDecimal? = null,
    val minRemainingAmount: BigDecimal? = null,
    val maxRemainingAmount: BigDecimal? = null,
    val observation: String? = null,
    val deliveryDeadlineFrom: LocalDate? = null,
    val deliveryDeadlineTo: LocalDate? = null,
    val dueWithinDays: Int? = null,
    val createdAtFrom: LocalDate? = null,
    val createdAtTo: LocalDate? = null,
    val downPaymentFrom: BigDecimal? = null,
    val downPaymentTo: BigDecimal? = null,
    val paidFrom: BigDecimal? = null,
    val paidTo: BigDecimal? = null,
    val remainingFrom: BigDecimal? = null,
    val remainingTo: BigDecimal? = null,
    val paymentStatus: SalePaymentStatus? = null,
    val deadlineStatus: SaleDeadlineStatus? = null,
    val paymentMethod: PaymentMethod? = null,
)
