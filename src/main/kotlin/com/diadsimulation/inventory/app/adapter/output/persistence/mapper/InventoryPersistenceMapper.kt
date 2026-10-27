package com.diadsimulation.inventory.app.adapter.output.persistence.mapper

import com.diadsimulation.inventory.app.adapter.output.persistence.entity.InventoryEntity
import com.diadsimulation.inventory.core.domain.model.Inventory

object InventoryPersistenceMapper {
    fun toEntity(inventory: Inventory): InventoryEntity =
        InventoryEntity(
            inventoryId = inventory.id,
            name = inventory.name,
            category = inventory.category,
            description = inventory.description,
            productId = inventory.productId,
            quantity = inventory.quantity,
            minimumQuantity = inventory.minimumQuantity,
            unit = inventory.unit,
            color = inventory.color,
            size = inventory.size,
            material = inventory.material,
            brand = inventory.brand,
            model = inventory.model,
            sku = inventory.sku,
            createdAt = inventory.createdAt,
            updatedAt = inventory.updatedAt,
        )

    fun toDomain(entity: InventoryEntity): Inventory =
        Inventory(
            id = entity.inventoryId,
            name = entity.name,
            category = entity.category,
            description = entity.description,
            productId = entity.productId,
            quantity = entity.quantity,
            minimumQuantity = entity.minimumQuantity,
            unit = entity.unit,
            color = entity.color,
            size = entity.size,
            material = entity.material,
            brand = entity.brand,
            model = entity.model,
            sku = entity.sku,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
}
