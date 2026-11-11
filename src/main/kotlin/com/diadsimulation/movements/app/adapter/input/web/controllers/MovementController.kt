package com.diadsimulation.movements.app.adapter.input.web.controllers

import com.diadsimulation.movements.app.adapter.input.web.dtos.responses.MovementsPageResponse
import com.diadsimulation.movements.app.adapter.input.web.swagger.MovementApi
import com.diadsimulation.movements.app.adapter.output.persistence.mappers.MovementWebMapper
import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import com.diadsimulation.movements.core.domain.models.MovementFilter
import com.diadsimulation.movements.core.ports.input.GetMovementsInputPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.OffsetDateTime

@RestController
@RequestMapping("/moviments")
class MovementController(
    private val getMovementsInputPort: GetMovementsInputPort,
    private val movementWebMapper: MovementWebMapper,
) : MovementApi {
    @GetMapping
    override fun getMovements(
        createdFrom: OffsetDateTime?,
        createdTo: OffsetDateTime?,
        minAmount: BigDecimal?,
        maxAmount: BigDecimal?,
        status: String?,
        origin: MovementOrigin?,
        direction: MovementDirection?,
        originId: String?,
        operation: MovementOperation?,
        description: String?,
        page: Int,
        size: Int,
        sortBy: String,
        sortDirection: String,
    ): ResponseEntity<MovementsPageResponse> {
        val filter =
            MovementFilter(
                createdFrom = createdFrom,
                createdTo = createdTo,
                minAmount = minAmount,
                maxAmount = maxAmount,
                status = status,
                origin = origin,
                direction = direction,
                originId = originId,
                operation = operation,
                description = description,
                page = page,
                size = size,
                sortBy = sortBy,
                sortDirection = sortDirection,
            )

        val result = getMovementsInputPort.execute(filter)

        return ResponseEntity.ok(
            movementWebMapper.toPageResponse(result),
        )
    }
}
