package com.diadsimulation.products.app.adapter.input.web.mappers

import com.diadsimulation.products.app.adapter.input.web.dtos.requests.CreateProductRequest
import com.diadsimulation.products.app.adapter.input.web.dtos.requests.UpdateProductRequest
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductFeeResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductPriceResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductsPageResponse
import com.diadsimulation.products.core.domain.enums.FeeChargeType
import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import com.diadsimulation.products.core.domain.model.CreateProductFeeCommand
import com.diadsimulation.products.core.domain.model.CreateProductPriceCommand
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductFee
import com.diadsimulation.products.core.domain.model.ProductPrice
import com.diadsimulation.products.core.domain.model.ProductsPage
import com.diadsimulation.products.core.domain.model.UpdateProductCommand
import com.diadsimulation.products.core.domain.model.UpdateProductFeeCommand
import com.diadsimulation.products.core.domain.model.UpdateProductPriceCommand
import com.diadsimulation.products.core.utils.PatchField
import org.springframework.stereotype.Component
import tools.jackson.databind.JsonNode
import java.math.BigDecimal
import java.util.UUID

@Component
class ProductWebMapper {
    fun toDomain(request: CreateProductRequest): Product =
        Product(
            code = request.code,
            name = request.name,
            category = request.category,
            description = request.description,
            minimumQuantity = request.minimumQuantity,
            pricingType = request.pricingType,
            shelfLifeDays = request.shelfLifeDays,
            productionLeadTimeMinDays = request.productionLeadTimeMinDays,
            productionLeadTimeMaxDays = request.productionLeadTimeMaxDays,
            active = request.active,
            prices =
                request.prices.map { price ->
                    ProductPrice(
                        minimumQuantity = price.minimumQuantity,
                        unitPrice = price.unitPrice,
                    )
                },
            fees =
                request.fees.map { fee ->
                    ProductFee(
                        name = fee.name,
                        amount = fee.amount,
                        chargeType = fee.chargeType,
                        required = fee.required,
                        active = fee.active,
                    )
                },
        )

    fun toCommand(request: UpdateProductRequest): UpdateProductCommand =
        UpdateProductCommand(
            name = patch(request.name) { text(it) },
            category =
                patch(request.category) {
                    enumValue<ProductCategory>(it)
                },
            description = nullablePatch(request.description) { text(it) },
            minimumQuantity = nullablePatch(request.minimumQuantity) { integer(it) },
            pricingType =
                patch(request.pricingType) {
                    enumValue<PricingType>(it)
                },
            shelfLifeDays = nullablePatch(request.shelfLifeDays) { integer(it) },
            productionLeadTimeMinDays =
                nullablePatch(request.productionLeadTimeMinDays) { integer(it) },
            productionLeadTimeMaxDays =
                nullablePatch(request.productionLeadTimeMaxDays) { integer(it) },
            active = patch(request.active) { boolean(it) },
            prices =
                patch(request.prices) { node ->
                    array(node).map { price ->
                        UpdateProductPriceCommand(
                            id = uuid(required(price, "id")),
                            minimumQuantity =
                                patch(price.get("minimumQuantity")) {
                                    integer(it)
                                },
                            unitPrice =
                                patch(price.get("unitPrice")) {
                                    decimal(it)
                                },
                        )
                    }
                },
            fees =
                patch(request.fees) { node ->
                    array(node).map { fee ->
                        UpdateProductFeeCommand(
                            id = uuid(required(fee, "id")),
                            name = patch(fee.get("name")) { text(it) },
                            amount = patch(fee.get("amount")) { decimal(it) },
                            chargeType =
                                patch(fee.get("chargeType")) {
                                    enumValue<FeeChargeType>(it)
                                },
                            required = patch(fee.get("required")) { boolean(it) },
                            active = patch(fee.get("active")) { boolean(it) },
                        )
                    }
                },
            addPrices =
                optionalArray(request.addPrices).map { price ->
                    CreateProductPriceCommand(
                        minimumQuantity = integer(required(price, "minimumQuantity")),
                        unitPrice = decimal(required(price, "unitPrice")),
                    )
                },
            removePriceIds = optionalArray(request.removePriceIds).map(::uuid),
            addFees =
                optionalArray(request.addFees).map { fee ->
                    CreateProductFeeCommand(
                        name = text(required(fee, "name")),
                        amount = decimal(required(fee, "amount")),
                        chargeType =
                            enumValue<FeeChargeType>(
                                required(fee, "chargeType"),
                            ),
                        required = fee.get("required")?.let(::boolean) ?: false,
                        active = fee.get("active")?.let(::boolean) ?: true,
                    )
                },
            removeFeeIds = optionalArray(request.removeFeeIds).map(::uuid),
        )

    private fun <T> patch(
        node: JsonNode?,
        convert: (JsonNode) -> T,
    ): PatchField<T> =
        when {
            node == null -> PatchField.Absent

            node.isNull -> throw IllegalArgumentException(
                "Este campo não permite valor nulo.",
            )

            else -> PatchField.Present(convert(node))
        }

    private fun <T> nullablePatch(
        node: JsonNode?,
        convert: (JsonNode) -> T,
    ): PatchField<T?> =
        when {
            node == null -> PatchField.Absent
            node.isNull -> PatchField.Present(null)
            else -> PatchField.Present(convert(node))
        }

    private fun array(node: JsonNode): List<JsonNode> {
        require(node.isArray) { "O campo deve ser uma lista." }
        return node.toList()
    }

    private fun optionalArray(node: JsonNode?): List<JsonNode> =
        when {
            node == null -> emptyList()

            node.isNull -> throw IllegalArgumentException(
                "A lista não pode ser nula.",
            )

            else -> array(node)
        }

    private fun required(
        node: JsonNode,
        field: String,
    ): JsonNode {
        require(node.isObject && node.hasNonNull(field)) {
            "O campo $field é obrigatório."
        }
        return node.get(field)
    }

    private fun text(node: JsonNode): String {
        require(node.isTextual) { "O campo deve ser um texto." }
        return node.textValue()
    }

    private fun integer(node: JsonNode): Int {
        require(node.isIntegralNumber && node.canConvertToInt()) {
            "O campo deve ser um número inteiro válido."
        }
        return node.intValue()
    }

    private fun decimal(node: JsonNode): BigDecimal {
        require(node.isNumber) { "O campo deve ser numérico." }
        return node.decimalValue()
    }

    private fun boolean(node: JsonNode): Boolean {
        require(node.isBoolean) { "O campo deve ser booleano." }
        return node.booleanValue()
    }

    private fun uuid(node: JsonNode): UUID {
        val value = text(node)
        return try {
            UUID.fromString(value)
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException("UUID inválido: $value")
        }
    }

    private inline fun <reified T : Enum<T>> enumValue(node: JsonNode): T {
        val value = text(node)
        return enumValues<T>().firstOrNull { it.name == value }
            ?: throw IllegalArgumentException("Valor inválido: $value")
    }

    fun toResponse(product: Product): ProductResponse =
        ProductResponse(
            id = product.id,
            code = product.code,
            name = product.name,
            category = product.category,
            description = product.description,
            minimumQuantity = product.minimumQuantity,
            pricingType = product.pricingType,
            shelfLifeDays = product.shelfLifeDays,
            productionLeadTimeMinDays = product.productionLeadTimeMinDays,
            productionLeadTimeMaxDays = product.productionLeadTimeMaxDays,
            active = product.active,
            prices =
                product.prices.map { price ->
                    ProductPriceResponse(
                        id = price.id,
                        minimumQuantity = price.minimumQuantity,
                        unitPrice = price.unitPrice,
                        createdAt = price.createdAt,
                        updatedAt = price.updatedAt,
                    )
                },
            fees =
                product.fees.map { fee ->
                    ProductFeeResponse(
                        id = fee.id,
                        name = fee.name,
                        amount = fee.amount,
                        chargeType = fee.chargeType,
                        required = fee.required,
                        active = fee.active,
                        createdAt = fee.createdAt,
                        updatedAt = fee.updatedAt,
                    )
                },
            createdAt = product.createdAt,
            updatedAt = product.updatedAt,
        )

    fun toPageResponse(productsPage: ProductsPage): ProductsPageResponse =
        ProductsPageResponse(
            content = productsPage.content.map(::toResponse),
            page = productsPage.page,
            size = productsPage.size,
            totalElementsThisPage = productsPage.totalElementsThisPage.toLong(),
            totalElements = productsPage.totalElements,
            totalPages = productsPage.totalPages,
        )
}
