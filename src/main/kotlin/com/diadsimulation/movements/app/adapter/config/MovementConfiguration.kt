package com.diadsimulation.movements.app.adapter.config

import com.diadsimulation.movements.core.ports.input.GetMovementsInputPort
import com.diadsimulation.movements.core.ports.output.MovementRepositoryOutputPort
import com.diadsimulation.movements.core.usecase.GetMovementsUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MovementConfiguration {
    @Bean
    fun getMovementsInputPort(movementRepositoryOutputPort: MovementRepositoryOutputPort): GetMovementsInputPort =
        GetMovementsUseCaseImplementation(movementRepositoryOutputPort)
}
