
package com.diadsimulation.products.app.adapter.input.web.swagger

import com.diadsimulation.auth.app.adapter.input.web.exception.ApiErrorResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.requests.CreateProductRequest
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(
    name = "Products",
    description = "Operações relacionadas ao gerenciamento de produtos",
)
interface ProductApi {
    @Operation(
        summary = "Cadastrar produto",
        description = "Cadastra um produto com suas faixas de preço e taxas.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Produto cadastrado com sucesso",
                content = [
                    Content(
                        schema = Schema(implementation = ProductResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = [
                    Content(
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Código comercial já cadastrado",
                content = [
                    Content(
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor",
                content = [
                    Content(
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun create(
        @Valid @RequestBody request: CreateProductRequest,
    ): ResponseEntity<ProductResponse>
}
