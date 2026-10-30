package com.diadsimulation.sales.app.adapter.output.persistence.mappers

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleEntity
import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleItemEntity
import com.diadsimulation.sales.core.domain.enums.PaymentMethod
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.domain.model.SaleItem
import org.springframework.stereotype.Component

@Component
class SalePersistenceMapper {
    fun toEntity(sale: Sale): SaleEntity =
        SaleEntity(
            id = sale.id,
            customerId = sale.customerId,
            deliveryDeadline = sale.deliveryDeadline,
            totalAmount = sale.totalAmount,
            downPaymentPercentage = sale.downPaymentPercentage,
            paidPercentage = sale.paidPercentage,
            paymentMethod = sale.paymentMethod.name,
            observation = sale.observation,
            createdAt = sale.createdAt,
            updatedAt = sale.updatedAt,
        )

    fun toItemEntities(sale: Sale): List<SaleItemEntity> =
        sale.items.map { item ->
            SaleItemEntity(
                id = item.id,
                saleId = sale.id,
                productId = item.productId,
                inventoryId = item.inventoryId,
                quantity = item.quantity,
                color = item.color,
                size = item.size,
                material = item.material,
                brand = item.brand,
                model = item.model,
            )
        }

    fun toDomain(
        entity: SaleEntity,
        items: List<SaleItemEntity>,
    ): Sale =
        Sale(
            id = entity.id,
            customerId = entity.customerId,
            items =
                items.map { item ->
                    SaleItem(
                        id = item.id,
                        productId = item.productId,
                        inventoryId = item.inventoryId,
                        quantity = item.quantity,
                        color = item.color,
                        size = item.size,
                        material = item.material,
                        brand = item.brand,
                        model = item.model,
                    )
                },
            deliveryDeadline = entity.deliveryDeadline,
            totalAmount = entity.totalAmount,
            downPaymentPercentage = entity.downPaymentPercentage,
            paidPercentage = entity.paidPercentage,
            paymentMethod = PaymentMethod.valueOf(entity.paymentMethod),
            observation = entity.observation,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
}
