package com.diadsimulation.movements.app.adapter.output.persistence.repositories

import com.diadsimulation.movements.app.adapter.output.persistence.entities.MovementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface MovementJpaRepository :
    JpaRepository<MovementEntity, UUID>,
    JpaSpecificationExecutor<MovementEntity>
