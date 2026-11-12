package com.diadsimulation.movements.core.ports.input

import com.diadsimulation.movements.core.domain.models.Movement
import java.util.UUID

interface GetMovementByIdInputPort {
    fun execute(id: UUID): Movement
}
