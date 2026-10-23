package com.diadsimulation.expenses.app.adapter.input.web.swagger

import com.diadsimulation.expenses.app.adapter.input.web.requests.CreateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(
    name = "Expenses",
    description = "Operações relacionadas ao gerenciamento de despesas",
)
interface ExpenseApi {
    @Operation(
        summary = "Cadastrar despesa",
        description =
            "Cadastra despesas gerais, vinculadas a um produto específico " +
                "ou a vários produtos da mesma categoria. " +
                "Despesas de estoque devem informar a quantidade.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Despesa cadastrada com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor",
            ),
        ],
    )
    fun create(
        @Valid @RequestBody request: CreateExpenseRequest,
    ): ResponseEntity<ExpenseResponse>
}
