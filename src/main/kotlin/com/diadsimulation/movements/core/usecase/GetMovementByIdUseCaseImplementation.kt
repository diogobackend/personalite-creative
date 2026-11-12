package com.diadsimulation.movements.core.usecase

import com.diadsimulation.movements.core.domain.models.Movement
import com.diadsimulation.movements.core.ports.input.GetMovementByIdInputPort
import com.diadsimulation.movements.core.ports.output.MovementRepositoryOutputPort
import java.util.NoSuchElementException
import java.util.UUID

class GetMovementByIdUseCaseImplementation(
    private val movementRepositoryOutputPort: MovementRepositoryOutputPort,
) : GetMovementByIdInputPort {
    override fun execute(id: UUID): Movement =
        movementRepositoryOutputPort.findById(id)
            ?: throw NoSuchElementException(
                "Movimentação não encontrada: $id",
            )
}
