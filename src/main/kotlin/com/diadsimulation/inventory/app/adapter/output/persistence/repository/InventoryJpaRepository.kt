package com.diadsimulation.inventory.app.adapter.output.persistence.repository

import com.diadsimulation.inventory.app.adapter.output.persistence.entity.InventoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface InventoryJpaRepository :
    JpaRepository<InventoryEntity, UUID>,
    JpaSpecificationExecutor<InventoryEntity> {
    fun existsBySku(sku: String): Boolean

    fun findBySku(sku: String): InventoryEntity?
}
