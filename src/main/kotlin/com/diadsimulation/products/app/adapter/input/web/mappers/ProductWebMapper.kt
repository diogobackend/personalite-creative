package com.diadsimulation.products.app.adapter.input.web.mappers

import com.diadsimulation.products.app.adapter.input.web.dtos.requests.CreateProductRequest
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductFeeResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductPriceResponse
import com.diadsimulation.products.app.adapter.input.web.dtos.responses.ProductResponse
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductFee
import com.diadsimulation.products.core.domain.model.ProductPrice
import org.springframework.stereotype.Component

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
}
