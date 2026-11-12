package com.diadsimulation.inventory.app.adapter.input.rest.dtos.request

import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.util.UUID

data class RegisterInventoryRequest(
    @field:NotBlank
    val name: String,
    val category: String,
    @field:NotNull
    val categoryId: UUID,
    val description: String? = null,
    val productId: UUID? = null,
    @field:PositiveOrZero
    val quantity: Int = 0,
    @field:PositiveOrZero
    val minimumQuantity: Int? = null,
    val unit: InventoryUnit? = null,
    val color: String? = null,
    val size: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val sku: String? = null,
)
