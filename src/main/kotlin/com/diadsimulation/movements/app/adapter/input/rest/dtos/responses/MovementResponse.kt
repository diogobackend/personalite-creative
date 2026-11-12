package com.diadsimulation.movements.app.adapter.input.rest.dtos.responses

import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class MovementResponse(
    val id: UUID,
    val originId: UUID,
    val originCode: String,
    val origin: MovementOrigin,
    val operation: MovementOperation,
    val direction: MovementDirection?,
    val status: String?,
    val previousValue: BigDecimal?,
    val currentValue: BigDecimal?,
    val amount: BigDecimal,
    val description: String?,
    val createdAt: OffsetDateTime,
)
