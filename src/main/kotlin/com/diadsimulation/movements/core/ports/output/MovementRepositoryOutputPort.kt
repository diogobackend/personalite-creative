package com.diadsimulation.movements.core.ports.output

import com.diadsimulation.movements.core.domain.models.Movement
import com.diadsimulation.movements.core.domain.models.MovementFilter
import com.diadsimulation.movements.core.domain.models.MovementPage
import java.util.UUID

interface MovementRepositoryOutputPort {
    fun save(movement: Movement): Movement

    fun findById(id: UUID): Movement?

    fun findAll(filter: MovementFilter): MovementPage
}
