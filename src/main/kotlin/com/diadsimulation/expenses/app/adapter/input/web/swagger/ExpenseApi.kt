package com.diadsimulation.expenses.app.adapter.input.web.swagger

import com.diadsimulation.expenses.app.adapter.input.web.requests.CreateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.requests.GetExpensesRequest
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseResponse
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpensesPageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Expenses", description = "Gerenciamento de despesas")
interface ExpenseApi {
    @Operation(
        summary = "Cadastrar despesa",
        description = "Cadastra uma despesa geral ou vinculada a produtos.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Despesa cadastrada com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
        ],
    )
    fun create(
        @Valid @RequestBody request: CreateExpenseRequest,
    ): ResponseEntity<ExpenseResponse>

    @Operation(
        summary = "Listar despesas",
        description = "Lista despesas com filtros dinâmicos combináveis, paginação e ordenação.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Despesas consultadas com sucesso"),
            ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        ],
    )
    fun findAll(
        @ParameterObject request: GetExpensesRequest,
    ): ResponseEntity<ExpensesPageResponse>
}
