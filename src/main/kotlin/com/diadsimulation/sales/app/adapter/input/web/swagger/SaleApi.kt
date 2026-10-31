package com.diadsimulation.sales.app.adapter.input.web.swagger

import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.CreateSaleRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.SearchSalesRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.CreateSaleResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SaleSummaryResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SearchSalesResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Tag(
    name = "Sales",
    description = "Gerenciamento de vendas",
)
interface SaleApi {
    @Operation(
        summary = "Cadastrar venda",
        description = """
            Cadastra uma venda vinculada a um cliente, contendo um ou mais produtos.

            Permite informar:
            - Quantidade individual de cada produto.
            - Características opcionais: cor, tamanho, material, marca e modelo.
            - Item de estoque opcional.
            - Prazo de entrega opcional.
            - Observação opcional da venda.
            - Valor total, percentual de entrada e percentual pago.

            As características informadas pertencem exclusivamente à venda,
            sem modificar o cadastro dos produtos ou do estoque.
        """,
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Venda cadastrada com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Cliente, produto ou item de estoque não encontrado",
            ),
        ],
    )
    fun create(
        @Valid @RequestBody request: CreateSaleRequest,
    ): ResponseEntity<CreateSaleResponse>

    @Operation(
        summary = "Consultar vendas",
        description = """
        Consulta vendas com filtros opcionais e combináveis.

        Permite filtrar por cliente, produto, características,
        valores financeiros, percentuais, datas e situações.

        Os filtros de produto devem corresponder ao mesmo item da venda.
        Os resultados são paginados e ordenados.
    """,
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Vendas consultadas com sucesso"),
            ApiResponse(responseCode = "400", description = "Parâmetros de consulta inválidos"),
        ],
    )
    fun searchSales(
        @ParameterObject
        @ModelAttribute
        request: SearchSalesRequest,
        @Parameter(description = "Página inicial: 0")
        @RequestParam(defaultValue = "0")
        page: Int,
        @Parameter(description = "Quantidade por página, máximo 100")
        @RequestParam(defaultValue = "20")
        size: Int,
        @Parameter(
            description = "Ordenar por createdAt, totalAmount, remainingAmount, deliveryDeadline ou paidPercentage",
        )
        @RequestParam(defaultValue = "createdAt")
        sortBy: String,
        @Parameter(description = "Direção ASC ou DESC")
        @RequestParam(defaultValue = "DESC")
        sortDirection: String,
    ): ResponseEntity<SearchSalesResponse>

    @Operation(
        summary = "Consultar venda por ID",
        description = "Consulta uma venda específica pelo ID, incluindo seus produtos e informações financeiras.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Venda encontrada com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "ID inválido",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Venda não encontrada",
            ),
        ],
    )
    fun findById(
        @Parameter(description = "ID da venda")
        @PathVariable id: UUID,
    ): ResponseEntity<SaleSummaryResponse>
}
