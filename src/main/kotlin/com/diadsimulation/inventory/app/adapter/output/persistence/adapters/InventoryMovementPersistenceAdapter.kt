package com.diadsimulation.inventory.app.adapter.output.persistence.adapters

import com.diadsimulation.inventory.app.adapter.output.persistence.mapper.InventoryMovementPersistenceMapper
import com.diadsimulation.inventory.app.adapter.output.persistence.repository.InventoryMovementJpaRepository
import com.diadsimulation.inventory.core.domain.model.InventoryMovement
import com.diadsimulation.inventory.core.ports.output.InventoryMovementRepositoryOutputPort
import org.springframework.stereotype.Component

@Component
class InventoryMovementPersistenceAdapter(
    private val inventoryMovementJpaRepository: InventoryMovementJpaRepository,
) : InventoryMovementRepositoryOutputPort {
    override fun save(movement: InventoryMovement): InventoryMovement {
        val entity = InventoryMovementPersistenceMapper.toEntity(movement)
        val savedEntity = inventoryMovementJpaRepository.save(entity)

        return InventoryMovementPersistenceMapper.toDomain(savedEntity)
    }
}
