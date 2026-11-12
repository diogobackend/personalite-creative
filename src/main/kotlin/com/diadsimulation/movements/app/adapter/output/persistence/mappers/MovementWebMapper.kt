package com.diadsimulation.movements.app.adapter.output.persistence.mappers

import com.diadsimulation.movements.app.adapter.input.rest.dtos.responses.MovementResponse
import com.diadsimulation.movements.app.adapter.input.rest.dtos.responses.MovementsPageResponse
import com.diadsimulation.movements.core.domain.models.Movement
import com.diadsimulation.movements.core.domain.models.MovementPage
import org.springframework.stereotype.Component

@Component
class MovementWebMapper {
    fun toResponse(movement: Movement): MovementResponse =
        MovementResponse(
            id = movement.id,
            originId = movement.originId,
            originCode = movement.originCode,
            origin = movement.origin,
            operation = movement.operation,
            direction = movement.direction,
            status = movement.status,
            previousValue = movement.previousValue,
            currentValue = movement.currentValue,
            amount = movement.amount,
            description = movement.description,
            createdAt = movement.createdAt,
        )

    fun toPageResponse(page: MovementPage): MovementsPageResponse =
        MovementsPageResponse(
            content = page.content.map(::toResponse),
            page = page.page,
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            first = page.first,
            last = page.last,
            empty = page.empty,
        )
}
