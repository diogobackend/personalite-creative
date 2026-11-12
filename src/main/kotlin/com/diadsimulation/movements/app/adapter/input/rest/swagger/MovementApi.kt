package com.diadsimulation.movements.app.adapter.input.rest.swagger

import com.diadsimulation.movements.app.adapter.input.rest.dtos.responses.MovementResponse
import com.diadsimulation.movements.app.adapter.input.rest.dtos.responses.MovementsPageResponse
import com.diadsimulation.movements.core.domain.enums.MovementDirection
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOrigin
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

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

    @Operation(summary = "Buscar movimentação financeira por ID")
    @ApiResponse(responseCode = "200", description = "Movimentação encontrada")
    @ApiResponse(responseCode = "404", description = "Movimentação não encontrada")
    fun getById(
        @PathVariable id: UUID,
    ): MovementResponse
}
