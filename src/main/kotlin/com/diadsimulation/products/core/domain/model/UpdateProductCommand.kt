package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import com.diadsimulation.products.core.utils.PatchField

data class UpdateProductCommand(
    val name: PatchField<String> = PatchField.Absent,
    val category: PatchField<ProductCategory> = PatchField.Absent,
    val description: PatchField<String?> = PatchField.Absent,
    val minimumQuantity: PatchField<Int?> = PatchField.Absent,
    val pricingType: PatchField<PricingType> = PatchField.Absent,
    val shelfLifeDays: PatchField<Int?> = PatchField.Absent,
    val productionLeadTimeMinDays: PatchField<Int?> = PatchField.Absent,
    val productionLeadTimeMaxDays: PatchField<Int?> = PatchField.Absent,
    val active: PatchField<Boolean> = PatchField.Absent,
    val prices: PatchField<List<UpdateProductPriceCommand>> = PatchField.Absent,
    val fees: PatchField<List<UpdateProductFeeCommand>> = PatchField.Absent,
)
