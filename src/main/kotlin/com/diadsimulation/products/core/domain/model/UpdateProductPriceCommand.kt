package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.utils.PatchField
import java.math.BigDecimal
import java.util.UUID

data class UpdateProductPriceCommand(
    val id: UUID,
    val minimumQuantity: PatchField<Int> = PatchField.Absent,
    val unitPrice: PatchField<BigDecimal> = PatchField.Absent,
)
