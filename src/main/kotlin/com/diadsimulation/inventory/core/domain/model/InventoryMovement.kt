package com.diadsimulation.inventory.core.domain.model

import com.diadsimulation.inventory.core.domain.enums.InventoryMovementType
import java.time.OffsetDateTime
import java.util.UUID

data class InventoryMovement(
    val id: UUID = UUID.randomUUID(),
    val inventoryId: UUID,
    val type: InventoryMovementType,
    val quantity: Int,
    val reason: String,
    val expenseId: UUID? = null,
    val saleId: UUID? = null,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
)
