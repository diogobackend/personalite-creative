package com.diadsimulation.movements.app.adapter.output.persistence.entities

import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(
    name = "movements",
    indexes = [
        Index(name = "idx_movements_created_at", columnList = "created_at"),
        Index(name = "idx_movements_origin", columnList = "origin, origin_id"),
        Index(name = "idx_movements_status", columnList = "status"),
        Index(name = "idx_movements_direction", columnList = "direction"),
        Index(name = "idx_movements_operation", columnList = "operation"),
        Index(name = "idx_movements_amount", columnList = "amount"),
    ],
)
class MovementEntity(
    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID,
    @Column(name = "origin_id", nullable = false)
    val originId: UUID,
    @Enumerated(STRING)
    @Column(nullable = false, length = 20)
    val origin: MovementOrigin,
    @Enumerated(STRING)
    @Column(nullable = false, length = 40)
    val operation: MovementOperation,
    @Enumerated(STRING)
    @Column(length = 10)
    val direction: MovementDirection?,
    @Column(length = 40)
    val status: String?,
    @Column(name = "previous_value", precision = 19, scale = 2)
    val previousValue: BigDecimal?,
    @Column(name = "current_value", precision = 19, scale = 2)
    val currentValue: BigDecimal?,
    @Column(nullable = false, precision = 19, scale = 2)
    val amount: BigDecimal,
    @Column(columnDefinition = "TEXT")
    val description: String?,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime,
)
