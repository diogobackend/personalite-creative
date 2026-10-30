package com.diadsimulation.sales.app.adapter.output.persistence.mappers

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleEntity
import com.diadsimulation.sales.app.adapter.output.persistence.entity.SaleItemEntity
import com.diadsimulation.sales.core.domain.model.Sale
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
}
