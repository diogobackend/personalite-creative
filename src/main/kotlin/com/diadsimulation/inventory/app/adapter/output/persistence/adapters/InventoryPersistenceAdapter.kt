package com.diadsimulation.inventory.app.adapter.output.persistence.adapters

import com.diadsimulation.inventory.app.adapter.output.persistence.mapper.InventoryPersistenceMapper
import com.diadsimulation.inventory.app.adapter.output.persistence.repository.InventoryJpaRepository
import com.diadsimulation.inventory.core.domain.model.Inventory
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class InventoryPersistenceAdapter(
    private val inventoryJpaRepository: InventoryJpaRepository,
) : InventoryRepositoryOutputPort {
    override fun save(inventory: Inventory): Inventory {
        val entity = InventoryPersistenceMapper.toEntity(inventory)
        val savedEntity = inventoryJpaRepository.save(entity)

        return InventoryPersistenceMapper.toDomain(savedEntity)
    }

    override fun findById(inventoryId: UUID): Inventory? =
        inventoryJpaRepository
            .findById(inventoryId)
            .map(InventoryPersistenceMapper::toDomain)
            .orElse(null)

    override fun existsBySku(sku: String): Boolean = inventoryJpaRepository.existsBySku(sku)
}
