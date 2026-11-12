package com.diadsimulation.categories.app.adapter.input.rest.swagger

import com.diadsimulation.categories.app.adapter.input.rest.dtos.requests.CreateCategoryRequest
import com.diadsimulation.categories.app.adapter.input.rest.dtos.requests.UpdateCategoryRequest
import com.diadsimulation.categories.app.adapter.input.rest.dtos.responses.CategoryResponse
import com.diadsimulation.categories.core.domain.enums.CategoryType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

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

    @Operation(
        summary = "Listar categorias",
        description = "Lista categorias com filtros opcionais e independentes por nome parcial e tipo, sem paginação.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Categorias consultadas com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Tipo de categoria inválido",
            ),
        ],
    )
    fun findAll(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) type: CategoryType?,
    ): ResponseEntity<List<CategoryResponse>>

    @Operation(
        summary = "Atualizar categoria",
        description = "Atualiza exclusivamente o nome de uma categoria.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Categoria atualizada com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Nome inválido",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Categoria não encontrada",
            ),
            ApiResponse(
                responseCode = "409",
                description = "Já existe uma categoria com esse nome e tipo",
            ),
        ],
    )
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateCategoryRequest,
    ): ResponseEntity<CategoryResponse>

    @Operation(
        summary = "Excluir categoria",
        description = "Exclui uma categoria pelo ID.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Categoria excluída com sucesso",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Categoria não encontrada",
            ),
        ],
    )
    fun delete(
        @PathVariable id: UUID,
    ): ResponseEntity<Void>
}
