package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import com.diadsimulation.products.core.utils.PatchField
import java.math.BigDecimal
import java.util.UUID

data class UpdateProductFeeCommand(
    val id: UUID,
    val name: PatchField<String> = PatchField.Absent,
    val amount: PatchField<BigDecimal> = PatchField.Absent,
    val chargeType: PatchField<FeeChargeType> = PatchField.Absent,
    val required: PatchField<Boolean> = PatchField.Absent,
    val active: PatchField<Boolean> = PatchField.Absent,
)
