package com.diadsimulation.movements.app.adapter.output.persistence.mappers

import com.diadsimulation.movements.app.adapter.output.persistence.entities.MovementEntity
import com.diadsimulation.movements.core.domain.models.Movement
import org.springframework.stereotype.Component

@Component
class MovementPersistenceMapper {
    fun toDomain(entity: MovementEntity): Movement =
        Movement(
            id = entity.id,
            originId = entity.originId,
            origin = entity.origin,
            operation = entity.operation,
            direction = entity.direction,
            status = entity.status,
            previousValue = entity.previousValue,
            currentValue = entity.currentValue,
            amount = entity.amount,
            description = entity.description,
            createdAt = entity.createdAt,
        )

    fun toEntity(domain: Movement): MovementEntity =
        MovementEntity(
            id = domain.id,
            originId = domain.originId,
            origin = domain.origin,
            operation = domain.operation,
            direction = domain.direction,
            status = domain.status,
            previousValue = domain.previousValue,
            currentValue = domain.currentValue,
            amount = domain.amount,
            description = domain.description,
            createdAt = domain.createdAt,
        )
}
