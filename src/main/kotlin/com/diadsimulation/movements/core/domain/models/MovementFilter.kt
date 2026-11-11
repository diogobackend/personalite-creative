package com.diadsimulation.movements.core.domain.models

import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime

data class MovementFilter(
    val createdFrom: OffsetDateTime? = null,
    val createdTo: OffsetDateTime? = null,
    val minAmount: BigDecimal? = null,
    val maxAmount: BigDecimal? = null,
    val status: String? = null,
    val origin: MovementOrigin? = null,
    val direction: MovementDirection? = null,
    val originId: String? = null,
    val operation: MovementOperation? = null,
    val description: String? = null,
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "createdAt",
    val sortDirection: String = "DESC",
) {
    init {
        require(page >= 0) {
            "Page cannot be negative"
        }

        require(size in 1..100) {
            "Size must be between 1 and 100"
        }

        require(createdFrom == null || createdTo == null || !createdFrom.isAfter(createdTo)) {
            "Invalid movement date range"
        }

        require(minAmount == null || minAmount >= ZERO) {
            "Minimum amount cannot be negative"
        }

        require(maxAmount == null || maxAmount >= ZERO) {
            "Maximum amount cannot be negative"
        }

        require(minAmount == null || maxAmount == null || minAmount <= maxAmount) {
            "Invalid movement amount range"
        }

        require(sortBy in setOf("createdAt", "amount", "origin", "operation", "status")) {
            "Invalid sorting field"
        }

        require(sortDirection.uppercase() in setOf("ASC", "DESC")) {
            "Invalid sorting direction"
        }
    }
}
