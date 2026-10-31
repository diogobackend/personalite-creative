package com.diadsimulation.sales.core.domain.model

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.UUID

data class SaleItem(
    val id: UUID = UUID.randomUUID(),
    val productId: UUID,
    val inventoryId: UUID? = null,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val additionalAmount: BigDecimal = ZERO,
    val color: String? = null,
    val size: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
) {
    val totalPrice: BigDecimal
        get() = unitPrice.multiply(quantity.toBigDecimal()) + additionalAmount
}
