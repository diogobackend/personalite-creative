package com.diadsimulation.expenses.app.adapter.output.persistence.mappers

import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpenseEntity
import com.diadsimulation.expenses.app.adapter.output.persistence.entities.ExpenseProductEntity
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpenseProduct
import org.springframework.stereotype.Component

@Component
class ExpensePersistenceMapper {
    fun toEntity(expense: Expense): ExpenseEntity {
        val entity =
            ExpenseEntity(
                id = expense.id,
                inventoryId = expense.inventoryId,
                name = expense.name,
                description = expense.description,
                value = expense.value,
                category = expense.category,
                allocationType = expense.allocationType,
                status = expense.status,
                quantity = expense.quantity,
                expenseDate = expense.expenseDate,
                createdAt = expense.createdAt,
                updatedAt = expense.updatedAt,
            )

        entity.products.addAll(
            expense.products.map { product ->
                ExpenseProductEntity(
                    expense = entity,
                    productId = product.productId,
                    allocatedValue = product.allocatedValue,
                )
            },
        )

        return entity
    }

    fun toDomain(entity: ExpenseEntity): Expense =
        Expense(
            id = entity.id,
            inventoryId = entity.inventoryId,
            name = entity.name,
            description = entity.description,
            value = entity.value,
            category = entity.category,
            allocationType = entity.allocationType,
            status = entity.status,
            quantity = entity.quantity,
            products =
                entity.products.map { product ->
                    ExpenseProduct(
                        productId = product.productId,
                        allocatedValue = product.allocatedValue,
                    )
                },
            expenseDate = entity.expenseDate,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
}
