package com.diadsimulation.products.app.adapter.input.web.swagger

import com.diadsimulation.auth.app.adapter.input.web.exception.ApiErrorResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.requests.CreateProductRequest
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductsPageResponse
import com.diadsimulation.products.core.domain.enums.FeeChargeType
import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal
import java.time.OffsetDateTime

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
                        mediaType = "application/json",
                        schema = Schema(implementation = ProductResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Código comercial já cadastrado",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun create(
        @Valid @RequestBody request: CreateProductRequest,
    ): ResponseEntity<ProductResponse>

    @Operation(
        summary = "Listar produtos",
        description =
            "Lista produtos com filtros combináveis, paginação e ordenação. " +
                "O código comercial possui prioridade absoluta e ignora todos os demais filtros. " +
                "As buscas por nome, descrição e nome da taxa são parciais e ignoram maiúsculas e minúsculas. " +
                "Os intervalos numéricos são inclusivos. " +
                "A ordenação por preço considera o menor preço unitário cadastrado de cada produto. " +
                "Produtos sem preço cadastrado aparecem no final.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Consulta realizada com sucesso, inclusive quando nenhum produto é encontrado",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ProductsPageResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Parâmetros de consulta inválidos",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun getProducts(
        @Parameter(description = "Código comercial de três dígitos. Ignora todos os demais filtros.")
        @RequestParam(required = false)
        code: String?,
        @Parameter(description = "Busca parcial pelo nome do produto.")
        @RequestParam(required = false)
        name: String?,
        @Parameter(description = "Busca parcial pela descrição do produto.")
        @RequestParam(required = false)
        description: String?,
        @Parameter(description = "Categoria exata do produto.")
        @RequestParam(required = false)
        category: ProductCategory?,
        @Parameter(description = "Situação do produto. Quando omitido, inclui ativos e inativos.")
        @RequestParam(required = false)
        active: Boolean?,
        @Parameter(description = "Tipo de precificação: FIXED, TIERED ou QUOTE.")
        @RequestParam(required = false)
        pricingType: PricingType?,
        @Parameter(description = "Busca parcial pelo nome da taxa.")
        @RequestParam(required = false)
        feeName: String?,
        @Parameter(description = "Tipo de cobrança: PER_ORDER, PER_UNIT ou FIRST_PURCHASE.")
        @RequestParam(required = false)
        chargeType: FeeChargeType?,
        @Parameter(description = "Limite inferior inclusivo da quantidade mínima.")
        @RequestParam(required = false)
        minQuantity: Int?,
        @Parameter(description = "Limite superior inclusivo da quantidade mínima.")
        @RequestParam(required = false)
        maxQuantity: Int?,
        @Parameter(description = "Validade mínima do produto em dias.")
        @RequestParam(required = false)
        minShelfLifeDays: Int?,
        @Parameter(description = "Validade máxima do produto em dias.")
        @RequestParam(required = false)
        maxShelfLifeDays: Int?,
        @Parameter(description = "Preço unitário mínimo de uma faixa de preço.")
        @RequestParam(required = false)
        minPrice: BigDecimal?,
        @Parameter(description = "Preço unitário máximo de uma faixa de preço.")
        @RequestParam(required = false)
        maxPrice: BigDecimal?,
        @Parameter(description = "Valor mínimo da taxa.")
        @RequestParam(required = false)
        minFeeAmount: BigDecimal?,
        @Parameter(description = "Valor máximo da taxa.")
        @RequestParam(required = false)
        maxFeeAmount: BigDecimal?,
        @Parameter(description = "Filtra produtos que possuem ou não taxas cadastradas.")
        @RequestParam(required = false)
        hasFees: Boolean?,
        @Parameter(description = "Data inicial de cadastro no formato ISO 8601.")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE_TIME)
        createdFrom: OffsetDateTime?,
        @Parameter(description = "Data final de cadastro no formato ISO 8601.")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE_TIME)
        createdTo: OffsetDateTime?,
        @Parameter(description = "Página solicitada, começando em zero. Padrão: 0.")
        @RequestParam(defaultValue = "0")
        page: Int,
        @Parameter(description = "Quantidade por página. Padrão: 20. Máximo: 100.")
        @RequestParam(defaultValue = "20")
        size: Int,
        @Parameter(
            description = "Campo de ordenação: name, code, createdAt, updatedAt, minimumQuantity, shelfLifeDays ou price.",
        )
        @RequestParam(defaultValue = "name")
        sortBy: String,
        @Parameter(description = "Direção da ordenação: ASC ou DESC.")
        @RequestParam(defaultValue = "ASC")
        sortDirection: String,
    ): ResponseEntity<ProductsPageResponse>
}
