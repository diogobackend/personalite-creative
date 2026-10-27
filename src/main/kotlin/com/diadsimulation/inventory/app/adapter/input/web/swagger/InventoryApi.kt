package com.diadsimulation.inventory.app.adapter.input.web.swagger

import com.diadsimulation.inventory.app.adapter.input.web.dtos.request.RegisterInventoryRequest
import com.diadsimulation.inventory.app.adapter.input.web.dtos.response.InventoryPageResponse
import com.diadsimulation.inventory.app.adapter.input.web.dtos.response.RegisterInventoryResponse
import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.products.core.domain.enums.ProductCategory
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.util.UUID

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

    @Operation(
        summary = "Listar itens do estoque",
        description = """
        Lista itens com filtros combináveis, paginação e ordenação.
        Os filtros são aplicados em conjunto (AND).
        Quando o SKU é informado, os demais filtros são ignorados.
        As datas são interpretadas em UTC.
    """,
    )
    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @GetMapping
    fun getInventory(
        @RequestParam(required = false)
        sku: String?,
        @RequestParam(required = false)
        name: String?,
        @RequestParam(required = false)
        category: ProductCategory?,
        @RequestParam(required = false)
        description: String?,
        @RequestParam(required = false)
        quantity: Int?,
        @RequestParam(required = false)
        minQuantity: Int?,
        @RequestParam(required = false)
        maxQuantity: Int?,
        @RequestParam(required = false)
        unit: InventoryUnit?,
        @RequestParam(required = false)
        color: String?,
        @RequestParam(required = false)
        size: InventorySize?,
        @RequestParam(required = false)
        material: String?,
        @RequestParam(required = false)
        brand: String?,
        @RequestParam(required = false)
        model: String?,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        createdAt: LocalDate?,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        createdFrom: LocalDate?,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        createdTo: LocalDate?,
        @RequestParam(defaultValue = "0")
        page: Int,
        @RequestParam(defaultValue = "20")
        pageSize: Int,
        @RequestParam(defaultValue = "name")
        sortBy: String,
        @RequestParam(defaultValue = "ASC")
        sortDirection: String,
    ): ResponseEntity<InventoryPageResponse>

    @Operation(
        summary = "Consultar item do estoque por ID",
        description = "Retorna os dados completos de um item do estoque pelo seu identificador.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item encontrado"),
            ApiResponse(responseCode = "400", description = "ID inválido"),
            ApiResponse(responseCode = "404", description = "Item não encontrado"),
        ],
    )
    @GetMapping("/{id}")
    fun getInventoryById(
        @PathVariable id: UUID,
    ): ResponseEntity<RegisterInventoryResponse>
}
