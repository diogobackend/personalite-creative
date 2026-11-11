package com.diadsimulation.movements.core.ports.input

import com.diadsimulation.movements.core.domain.models.MovementFilter
import com.diadsimulation.movements.core.domain.models.MovementPage

interface GetMovementsInputPort {
    fun execute(filter: MovementFilter): MovementPage
}
