package com.diadsimulation.expenses.app.adapter.input.web.mappers

import com.diadsimulation.expenses.app.adapter.input.web.requests.CreateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseProductResponse
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseResponse
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpenseProduct
import org.springframework.stereotype.Component

@Component
class ExpenseWebMapper {
    fun toDomain(request: CreateExpenseRequest): Expense =
        Expense(
            name = request.name,
            description = request.description,
            value = request.value,
            category = request.category,
            allocationType = request.allocationType,
            status = request.status,
            quantity = request.quantity,
            products =
                request.products.map { product ->
                    ExpenseProduct(
                        productId = product.productId,
                        allocatedValue = product.allocatedValue,
                    )
                },
            expenseDate = request.expenseDate,
        )

    fun toResponse(expense: Expense): ExpenseResponse =
        ExpenseResponse(
            id = expense.id,
            name = expense.name,
            description = expense.description,
            value = expense.value,
            category = expense.category,
            allocationType = expense.allocationType,
            status = expense.status,
            quantity = expense.quantity,
            products =
                expense.products.map { product ->
                    ExpenseProductResponse(
                        productId = product.productId,
                        allocatedValue = product.allocatedValue,
                    )
                },
            expenseDate = expense.expenseDate,
            createdAt = expense.createdAt,
            updatedAt = expense.updatedAt,
        )
}
