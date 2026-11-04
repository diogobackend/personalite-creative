package com.diadsimulation.categories.app.adapter.input.web.swagger

import com.diadsimulation.categories.app.adapter.input.web.dtos.requests.CreateCategoryRequest
import com.diadsimulation.categories.app.adapter.input.web.dtos.responses.CategoryResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Categories", description = "Gerenciamento de categorias")
interface CategoryApi {
    @Operation(
        summary = "Cadastrar categoria",
        description = "Cadastra uma categoria de produto, estoque ou despesa.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Categoria cadastrada com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
            ),
            ApiResponse(
                responseCode = "409",
                description = "Categoria já cadastrada para o mesmo tipo",
            ),
        ],
    )
    fun create(
        @Valid @RequestBody request: CreateCategoryRequest,
    ): ResponseEntity<CategoryResponse>
}
