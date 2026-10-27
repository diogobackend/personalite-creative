package com.diadsimulation.inventory.app.adapter.output.persistence.repository

import com.diadsimulation.inventory.app.adapter.output.persistence.entity.InventoryMovementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InventoryMovementJpaRepository : JpaRepository<InventoryMovementEntity, UUID>
