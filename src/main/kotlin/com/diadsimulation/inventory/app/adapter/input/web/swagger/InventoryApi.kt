package com.diadsimulation.inventory.app.adapter.input.web.swagger

import com.diadsimulation.inventory.app.adapter.input.web.request.RegisterInventoryRequest
import com.diadsimulation.inventory.app.adapter.input.web.response.RegisterInventoryResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(
    name = "Inventory",
    description = "Gerenciamento de estoque",
)
interface InventoryApi {
    @Operation(
        summary = "Cadastrar item no estoque",
        description = "Cadastra um item com suas características e quantidade inicial.",
    )
    @ApiResponse(
        responseCode = "201",
        description = "Item cadastrado com sucesso",
    )
    @ApiResponse(
        responseCode = "400",
        description = "Dados inválidos",
    )
    @ApiResponse(
        responseCode = "403",
        description = "Acesso negado",
    )
    @PostMapping
    fun register(
        @Valid @RequestBody request: RegisterInventoryRequest,
    ): ResponseEntity<RegisterInventoryResponse>
}
