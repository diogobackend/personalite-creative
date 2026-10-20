
package com.diadsimulation.products.app.adapter.output.persistence.mappers

import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductEntity
import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductFeeEntity
import com.diadsimulation.products.app.adapter.output.persistence.entities.ProductPriceEntity
import com.diadsimulation.products.core.domain.model.Product
import com.diadsimulation.products.core.domain.model.ProductFee
import com.diadsimulation.products.core.domain.model.ProductPrice
import org.springframework.stereotype.Component

@Component
class ProductPersistenceMapper {
    fun toEntity(product: Product): ProductEntity {
        val entity =
            ProductEntity(
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
                createdAt = product.createdAt,
                updatedAt = product.updatedAt,
            )

        entity.prices.addAll(
            product.prices.map { price ->
                ProductPriceEntity(
                    id = price.id,
                    product = entity,
                    minimumQuantity = price.minimumQuantity,
                    unitPrice = price.unitPrice,
                    createdAt = price.createdAt,
                    updatedAt = price.updatedAt,
                )
            },
        )

        entity.fees.addAll(
            product.fees.map { fee ->
                ProductFeeEntity(
                    id = fee.id,
                    product = entity,
                    name = fee.name,
                    amount = fee.amount,
                    chargeType = fee.chargeType,
                    required = fee.required,
                    active = fee.active,
                    createdAt = fee.createdAt,
                    updatedAt = fee.updatedAt,
                )
            },
        )

        return entity
    }

    fun toDomain(entity: ProductEntity): Product =
        Product(
            id = entity.id,
            code = entity.code,
            name = entity.name,
            category = entity.category,
            description = entity.description,
            minimumQuantity = entity.minimumQuantity,
            pricingType = entity.pricingType,
            shelfLifeDays = entity.shelfLifeDays,
            productionLeadTimeMinDays = entity.productionLeadTimeMinDays,
            productionLeadTimeMaxDays = entity.productionLeadTimeMaxDays,
            active = entity.active,
            prices = entity.prices.map(::toPriceDomain),
            fees = entity.fees.map(::toFeeDomain),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    private fun toPriceDomain(entity: ProductPriceEntity): ProductPrice =
        ProductPrice(
            id = entity.id,
            minimumQuantity = entity.minimumQuantity,
            unitPrice = entity.unitPrice,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    private fun toFeeDomain(entity: ProductFeeEntity): ProductFee =
        ProductFee(
            id = entity.id,
            name = entity.name,
            amount = entity.amount,
            chargeType = entity.chargeType,
            required = entity.required,
            active = entity.active,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
}
