package com.diadsimulation.inventory.app.adapter.input.web.mapper

import com.diadsimulation.inventory.app.adapter.input.web.dtos.request.RegisterInventoryRequest
import com.diadsimulation.inventory.app.adapter.input.web.dtos.response.RegisterInventoryResponse
import com.diadsimulation.inventory.core.domain.model.Inventory

object RegisterInventoryMapper {
    fun toDomain(request: RegisterInventoryRequest): Inventory =
        Inventory(
            name = request.name,
            category = request.category,
            description = request.description,
            productId = request.productId,
            quantity = request.quantity,
            minimumQuantity = request.minimumQuantity,
            unit = request.unit,
            color = request.color,
            size = request.size,
            material = request.material,
            brand = request.brand,
            model = request.model,
            sku = request.sku,
        )

    fun toResponse(inventory: Inventory): RegisterInventoryResponse =
        RegisterInventoryResponse(
            id = inventory.id,
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
}
