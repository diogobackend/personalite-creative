package com.diadsimulation.sales.app.adapter.input.web.dtos.requests

import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus
import com.diadsimulation.sales.core.domain.enums.SalePaymentStatus
import com.diadsimulation.sales.core.domain.model.SaleSearchFilter
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class SearchSalesRequest(
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
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val deliveryDeadlineFrom: LocalDate? = null,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val deliveryDeadlineTo: LocalDate? = null,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val createdAtFrom: LocalDate? = null,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val createdAtTo: LocalDate? = null,
    val dueWithinDays: Int? = null,
    val downPaymentFrom: BigDecimal? = null,
    val downPaymentTo: BigDecimal? = null,
    val paidFrom: BigDecimal? = null,
    val paidTo: BigDecimal? = null,
    val remainingFrom: BigDecimal? = null,
    val remainingTo: BigDecimal? = null,
    val paymentStatus: SalePaymentStatus? = null,
    val deadlineStatus: SaleDeadlineStatus? = null,
    val paymentMethod: PaymentMethod? = null,
) {
    fun toFilter() =
        SaleSearchFilter(
            search = search,
            saleId = saleId,
            customer = customer,
            productName = productName,
            productCommercialCode = productCommercialCode,
            color = color,
            brand = brand,
            model = model,
            size = size,
            material = material,
            minTotalAmount = minTotalAmount,
            maxTotalAmount = maxTotalAmount,
            minRemainingAmount = minRemainingAmount,
            maxRemainingAmount = maxRemainingAmount,
            observation = observation,
            deliveryDeadlineFrom = deliveryDeadlineFrom,
            deliveryDeadlineTo = deliveryDeadlineTo,
            createdAtFrom = createdAtFrom,
            createdAtTo = createdAtTo,
            dueWithinDays = dueWithinDays,
            downPaymentFrom = downPaymentFrom,
            downPaymentTo = downPaymentTo,
            paidFrom = paidFrom,
            paidTo = paidTo,
            remainingFrom = remainingFrom,
            remainingTo = remainingTo,
            paymentStatus = paymentStatus,
            deadlineStatus = deadlineStatus,
            paymentMethod = paymentMethod,
        )
}
