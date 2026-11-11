package com.diadsimulation.movements.app.adapter.input.web.swagger

import com.diadsimulation.movements.app.adapter.input.web.dtos.responses.MovementsPageResponse
import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal
import java.time.OffsetDateTime

@Tag(name = "Movements", description = "Financial movements history")
interface MovementApi {
    @Operation(summary = "Get financial movements with filters and pagination")
    fun getMovements(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE_TIME)
        createdFrom: OffsetDateTime?,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE_TIME)
        createdTo: OffsetDateTime?,
        @RequestParam(required = false)
        minAmount: BigDecimal?,
        @RequestParam(required = false)
        maxAmount: BigDecimal?,
        @RequestParam(required = false)
        status: String?,
        @RequestParam(required = false)
        origin: MovementOrigin?,
        @RequestParam(required = false)
        direction: MovementDirection?,
        @RequestParam(required = false)
        originId: String?,
        @RequestParam(required = false)
        operation: MovementOperation?,
        @RequestParam(required = false)
        description: String?,
        @RequestParam(defaultValue = "0")
        page: Int,
        @RequestParam(defaultValue = "20")
        size: Int,
        @RequestParam(defaultValue = "createdAt")
        sortBy: String,
        @RequestParam(defaultValue = "DESC")
        sortDirection: String,
    ): ResponseEntity<MovementsPageResponse>
}
