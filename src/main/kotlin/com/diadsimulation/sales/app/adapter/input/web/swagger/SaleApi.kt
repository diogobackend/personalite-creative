package com.diadsimulation.sales.app.adapter.input.web.swagger

import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.CreateSaleRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.CreateSaleResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Sales", description = "Gerenciamento de vendas")
interface SaleApi {
    @Operation(
        summary = "Cadastrar venda",
        description = "Cadastra uma venda vinculada a um cliente, com produtos e itens de estoque opcionais.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Venda cadastrada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
            ApiResponse(responseCode = "404", description = "Cliente, produto ou item de estoque não encontrado"),
        ],
    )
    fun create(
        @Valid @RequestBody request: CreateSaleRequest,
    ): ResponseEntity<CreateSaleResponse>
}
