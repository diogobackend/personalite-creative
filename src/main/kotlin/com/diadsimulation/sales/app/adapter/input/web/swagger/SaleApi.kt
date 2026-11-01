package com.diadsimulation.sales.app.adapter.input.web.swagger

import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.CreateSaleRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.SearchSalesRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.UpdateSaleRequest
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

    @Operation(
        summary = "Atualizar venda",
        description = """
        Atualiza parcialmente uma venda existente.

        Permite alterar um, vários ou todos os campos da venda,
        mantendo os valores atuais dos campos não informados.

        Permite atualizar:
        - Cliente vinculado à venda.
        - Carrinho completo, incluindo adição, remoção e substituição de produtos.
        - Quantidade individual de cada produto.
        - Preço unitário de cada produto.
        - Descontos e acréscimos individuais dos produtos.
        - Características opcionais: cor, tamanho, material, marca e modelo.
        - Item de estoque vinculado ao produto.
        - Prazo de entrega, incluindo sua remoção.
        - Observação da venda, incluindo sua remoção.
        - Percentual de entrada.
        - Percentual já pago.
        - Forma de pagamento.

        Regras:
        - Todos os campos são opcionais.
        - Campos não informados permanecem inalterados.
        - Quando informado, o carrinho substitui a lista atual de produtos.
        - O carrinho deve conter pelo menos um produto.
        - A quantidade de cada produto deve ser maior que zero.
        - O preço unitário e o valor final de cada produto devem ser positivos.
        - Descontos e acréscimos são considerados no cálculo do valor final.
        - O valor total da venda é recalculado automaticamente.
        - Os percentuais de entrada e pagamento devem estar entre 0 e 100.
        - O prazo de entrega e a observação podem ser removidos enviando null.
        - As características dos produtos pertencem exclusivamente à venda,
          sem modificar o cadastro dos produtos ou do estoque.
        - O ID e a data de criação da venda são preservados.
        - A data de atualização é renovada automaticamente.
    """,
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Venda atualizada com sucesso",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos ou regras de negócio não atendidas",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Venda, cliente, produto ou item de estoque não encontrado",
            ),
        ],
    )
    fun update(
        @Parameter(description = "ID da venda que será atualizada")
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateSaleRequest,
    ): ResponseEntity<CreateSaleResponse>
}
