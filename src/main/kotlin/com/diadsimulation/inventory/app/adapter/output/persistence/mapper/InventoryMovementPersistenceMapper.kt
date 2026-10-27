package com.diadsimulation.inventory.app.adapter.output.persistence.mapper

import com.diadsimulation.inventory.app.adapter.output.persistence.entity.InventoryMovementEntity
import com.diadsimulation.inventory.core.domain.model.InventoryMovement

object InventoryMovementPersistenceMapper {
    fun toEntity(movement: InventoryMovement): InventoryMovementEntity =
        InventoryMovementEntity(
            movementId = movement.id,
            inventoryId = movement.inventoryId,
            type = movement.type,
            quantity = movement.quantity,
            reason = movement.reason,
            expenseId = movement.expenseId,
            saleId = movement.saleId,
            createdAt = movement.createdAt,
        )

    fun toDomain(entity: InventoryMovementEntity): InventoryMovement =
        InventoryMovement(
            id = entity.movementId,
            inventoryId = entity.inventoryId,
            type = entity.type,
            quantity = entity.quantity,
            reason = entity.reason,
            expenseId = entity.expenseId,
            saleId = entity.saleId,
            createdAt = entity.createdAt,
        )
}
