package com.diadsimulation.movements.core.usecase

import com.diadsimulation.movements.core.domain.models.MovementFilter
import com.diadsimulation.movements.core.domain.models.MovementPage
import com.diadsimulation.movements.core.ports.input.GetMovementsInputPort
import com.diadsimulation.movements.core.ports.output.MovementRepositoryOutputPort

class GetMovementsUseCaseImplementation(
    private val movementRepositoryOutputPort: MovementRepositoryOutputPort,
) : GetMovementsInputPort {
    override fun execute(filter: MovementFilter): MovementPage = movementRepositoryOutputPort.findAll(filter)
}
