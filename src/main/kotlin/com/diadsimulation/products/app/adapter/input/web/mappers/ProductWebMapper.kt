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
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductFee
import com.diadsimulation.products.core.domain.model.ProductPrice
import com.diadsimulation.products.core.domain.model.ProductsPage
import com.diadsimulation.products.core.domain.model.UpdateProductCommand
import com.diadsimulation.products.core.domain.model.UpdateProductFeeCommand
import com.diadsimulation.products.core.domain.model.UpdateProductPriceCommand
import com.diadsimulation.products.core.utils.PatchField
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
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
            name = patch(request.name) { it.asText() },
            category =
                patch(request.category) {
                    ProductCategory.valueOf(it.asText())
                },
            description = nullablePatch(request.description) { it.asText() },
            minimumQuantity = nullablePatch(request.minimumQuantity) { it.intValue() },
            pricingType =
                patch(request.pricingType) {
                    PricingType.valueOf(it.asText())
                },
            shelfLifeDays = nullablePatch(request.shelfLifeDays) { it.intValue() },
            productionLeadTimeMinDays =
                nullablePatch(request.productionLeadTimeMinDays) { it.intValue() },
            productionLeadTimeMaxDays =
                nullablePatch(request.productionLeadTimeMaxDays) { it.intValue() },
            active = patch(request.active) { it.booleanValue() },
            prices =
                patch(request.prices) { node ->
                    require(node.isArray) { "prices deve ser uma lista." }

                    node.map { price ->
                        UpdateProductPriceCommand(
                            id = UUID.fromString(price.required("id").asText()),
                            minimumQuantity =
                                patch(price.get("minimumQuantity")) {
                                    it.intValue()
                                },
                            unitPrice =
                                patch(price.get("unitPrice")) {
                                    it.decimalValue()
                                },
                        )
                    }
                },
            fees =
                patch(request.fees) { node ->
                    require(node.isArray) { "fees deve ser uma lista." }

                    node.map { fee ->
                        UpdateProductFeeCommand(
                            id = UUID.fromString(fee.required("id").asText()),
                            name = patch(fee.get("name")) { it.asText() },
                            amount = patch(fee.get("amount")) { it.decimalValue() },
                            chargeType =
                                patch(fee.get("chargeType")) {
                                    FeeChargeType.valueOf(it.asText())
                                },
                            required = patch(fee.get("required")) { it.booleanValue() },
                            active = patch(fee.get("active")) { it.booleanValue() },
                        )
                    }
                },
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
