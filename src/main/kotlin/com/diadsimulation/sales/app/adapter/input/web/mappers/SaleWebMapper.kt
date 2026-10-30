package com.diadsimulation.sales.app.adapter.input.web.mappers

import com.diadsimulation.sales.app.adapter.input.web.dtos.requests.CreateSaleRequest
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.CreateSaleItemResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.CreateSaleResponse
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.domain.model.SaleItem
import org.springframework.stereotype.Component

@Component
class SaleWebMapper {
    fun toDomain(request: CreateSaleRequest): Sale =
        Sale(
            customerId = request.customerId,
            deliveryDeadline = request.deliveryDeadline,
            totalAmount = request.totalAmount,
            downPaymentPercentage = request.downPaymentPercentage,
            paidPercentage = request.paidPercentage,
            paymentMethod = request.paymentMethod,
            observation = request.observation,
            items =
                request.items.map { item ->
                    SaleItem(
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
        )

    fun toResponse(sale: Sale): CreateSaleResponse =
        CreateSaleResponse(
            id = sale.id,
            customerId = sale.customerId,
            deliveryDeadline = sale.deliveryDeadline,
            totalAmount = sale.totalAmount,
            downPaymentPercentage = sale.downPaymentPercentage,
            paidPercentage = sale.paidPercentage,
            paymentMethod = sale.paymentMethod,
            observation = sale.observation,
            items =
                sale.items.map { item ->
                    CreateSaleItemResponse(
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
            createdAt = sale.createdAt,
            updatedAt = sale.updatedAt,
        )
}
