package com.diadsimulation.movements.core.domain.models

import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import java.util.UUID

data class Movement(
    val id: UUID,
    val originId: UUID,
    val origin: MovementOrigin,
    val operation: MovementOperation,
    val direction: MovementDirection?,
    val status: String?,
    val previousValue: BigDecimal?,
    val currentValue: BigDecimal?,
    val amount: BigDecimal,
    val description: String?,
    val createdAt: OffsetDateTime,
) {
    init {
        require(amount >= ZERO) {
            "Movement amount cannot be negative"
        }
    }

    val originCode: String
        get() = originId.toString().take(5)
}
