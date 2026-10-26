package com.diadsimulation.expenses.app.adapter.input.web.mappers

import com.diadsimulation.expenses.app.adapter.input.web.requests.CreateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.requests.GetExpensesRequest
import com.diadsimulation.expenses.app.adapter.input.web.requests.UpdateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseProductResponse
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseResponse
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpensesPageResponse
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpenseProduct
import com.diadsimulation.expenses.core.domain.model.ExpenseUpdate
import com.diadsimulation.expenses.core.domain.model.ExpensesPage
import com.diadsimulation.expenses.core.domain.model.GetExpensesFilter
import org.springframework.stereotype.Component
import java.time.OffsetDateTime.now

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
            createdAt = request.createdAt ?: now(),
        )

    fun toFilter(request: GetExpensesRequest): GetExpensesFilter =
        GetExpensesFilter(
            name = request.name,
            description = request.description,
            value = request.value,
            minValue = request.minValue,
            maxValue = request.maxValue,
            category = request.category,
            allocationType = request.allocationType,
            status = request.status,
            productId = request.productId,
            quantity = request.quantity,
            minQuantity = request.minQuantity,
            maxQuantity = request.maxQuantity,
            expenseDate = request.expenseDate,
            expenseDateFrom = request.expenseDateFrom,
            expenseDateTo = request.expenseDateTo,
            createdFrom = request.createdFrom,
            createdTo = request.createdTo,
            updatedFrom = request.updatedFrom,
            updatedTo = request.updatedTo,
            createdAt = request.createdAt,
            page = request.page,
            size = request.size,
            sortBy = request.sortBy,
            sortDirection = request.sortDirection,
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

    fun toPageResponse(page: ExpensesPage): ExpensesPageResponse =
        ExpensesPageResponse(
            content = page.content.map(::toResponse),
            page = page.page,
            size = page.size,
            totalElements = page.totalElements,
            totalElementsThisPage = page.totalElementsThisPage,
            totalPages = page.totalPages,
        )

    fun toUpdate(
        request: UpdateExpenseRequest,
        fieldsPresent: Set<String>,
    ): ExpenseUpdate =
        ExpenseUpdate(
            name = request.name,
            description = request.description,
            value = request.value,
            category = request.category,
            allocationType = request.allocationType,
            status = request.status,
            quantity = request.quantity,
            products =
                request.products?.map {
                    ExpenseProduct(
                        productId = it.productId,
                        allocatedValue = it.allocatedValue,
                    )
                },
            expenseDate = request.expenseDate,
            fieldsPresent = fieldsPresent,
        )
}
