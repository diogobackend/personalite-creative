package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.CATEGORY_PRODUCTS
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.GENERAL
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.SPECIFIC_PRODUCT
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.CANCELLED
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PAID
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PARTIALLY_PAID
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PENDING
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpenseUpdate
import com.diadsimulation.expenses.core.ports.input.UpdateExpenseInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseCategoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseStockOutputPort
import com.diadsimulation.expenses.core.ports.output.ProductExpenseValidationOutputPort
import com.diadsimulation.expenses.core.usecase.UpdateExpenseUseCaseImplementation.Constants.INVENTORY_ITEM_CATEGORY
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import java.util.Collections.emptyList
import java.util.NoSuchElementException
import java.util.UUID

class UpdateExpenseUseCaseImplementation(
    private val expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
    private val productExpenseValidationOutputPort: ProductExpenseValidationOutputPort,
    private val expenseStockOutputPort: ExpenseStockOutputPort,
    private val expenseCategoryOutputPort: ExpenseCategoryOutputPort,
) : UpdateExpenseInputPort {
    object Constants {
        const val INVENTORY_ITEM_CATEGORY = "item de estoque"
    }

    override fun execute(
        id: UUID,
        update: ExpenseUpdate,
    ): Expense {
        val currentExpense =
            expenseRepositoryOutputPort.findById(id)
                ?: throw NoSuchElementException(
                    "Despesa não encontrada para o ID: $id",
                )

        validateRequiredFields(update)

        require(currentExpense.status != CANCELLED) {
            "Não é permitido alterar uma despesa cancelada."
        }

        val allocationType =
            update.allocationType ?: currentExpense.allocationType

        val products =
            when {
                update.has("products") -> {
                    requireNotNull(update.products) {
                        "A lista de produtos não pode ser nula."
                    }
                }

                update.has("allocationType") &&
                    allocationType == GENERAL -> {
                    emptyList()
                }

                else -> {
                    currentExpense.products
                }
            }

        val value =
            when {
                update.has("value") -> {
                    requireNotNull(update.value) {
                        "O valor da despesa não pode ser nulo."
                    }
                }

                update.has("products") &&
                    allocationType != GENERAL -> {
                    products.fold(ZERO) { total, product ->
                        total.add(product.allocatedValue)
                    }
                }

                else -> {
                    currentExpense.value
                }
            }

        val paidAmount =
            if (update.has("paidAmount")) {
                requireNotNull(update.paidAmount) {
                    "O valor pago não pode ser nulo."
                }
            } else {
                currentExpense.paidAmount
            }

        require(paidAmount >= ZERO && paidAmount <= value) {
            "O valor pago deve estar entre zero e o valor total da despesa."
        }

        val status =
            if (update.status == CANCELLED) {
                CANCELLED
            } else {
                when {
                    paidAmount.compareTo(ZERO) == 0 -> PENDING
                    paidAmount.compareTo(value) == 0 -> PAID
                    else -> PARTIALLY_PAID
                }
            }

        val updatedExpense =
            currentExpense.copy(
                name = update.name ?: currentExpense.name,
                inventoryId =
                    when {
                        update.has("inventoryId") -> update.inventoryId
                        update.category != null && update.category != INVENTORY_ITEM_CATEGORY -> null
                        else -> currentExpense.inventoryId
                    },
                description =
                    if (update.has("description")) {
                        update.description
                    } else {
                        currentExpense.description
                    },
                value = value,
                paidAmount = paidAmount,
                category = update.category ?: currentExpense.category,
                categoryId = update.categoryId ?: currentExpense.categoryId,
                allocationType = allocationType,
                status = status,
                quantity =
                    when {
                        update.has("quantity") -> update.quantity
                        update.category != null && update.category != INVENTORY_ITEM_CATEGORY -> null
                        else -> currentExpense.quantity
                    },
                products = products,
                expenseDate = update.expenseDate ?: currentExpense.expenseDate,
                updatedAt = OffsetDateTime.now(),
            )

        if (update.has("categoryId")) {
            val categoryId =
                requireNotNull(updatedExpense.categoryId) {
                    "O ID da categoria é obrigatório."
                }

            require(expenseCategoryOutputPort.existsByIdAndTypeExpense(categoryId)) {
                "A categoria informada não existe ou não pertence ao tipo EXPENSE."
            }
        }

        validate(updatedExpense)

        val savedExpense = expenseRepositoryOutputPort.save(updatedExpense)

        expenseStockOutputPort.updateEntry(
            previousExpense = currentExpense,
            updatedExpense = savedExpense,
        )

        return savedExpense
    }

    private fun validateRequiredFields(update: ExpenseUpdate) {
        require(!update.has("paidAmount") || update.paidAmount != null) {
            "O valor pago não pode ser nulo."
        }

        require(!update.has("categoryId") || update.categoryId != null) {
            "O ID da categoria não pode ser nulo."
        }

        require(!update.has("name") || update.name != null) {
            "O nome da despesa não pode ser nulo."
        }

        require(!update.has("value") || update.value != null) {
            "O valor da despesa não pode ser nulo."
        }

        require(!update.has("category") || update.category != null) {
            "A categoria da despesa não pode ser nula."
        }

        require(!update.has("allocationType") || update.allocationType != null) {
            "O tipo de alocação não pode ser nulo."
        }

        require(!update.has("status") || update.status != null) {
            "O status da despesa não pode ser nulo."
        }

        require(!update.has("expenseDate") || update.expenseDate != null) {
            "A data da despesa não pode ser nula."
        }

        require(!update.has("products") || update.products != null) {
            "A lista de produtos não pode ser nula."
        }
    }

    private fun validate(expense: Expense) {
        require(
            expense.name.isNotBlank() &&
                expense.name.length <= 150,
        ) {
            "O nome da despesa deve possuir entre 1 e 150 caracteres."
        }

        require(expense.value > ZERO) {
            "O valor da despesa deve ser maior que zero."
        }

        validateStock(expense)
        validateProducts(expense)
    }

    private fun validateStock(expense: Expense) {
        if (expense.category == INVENTORY_ITEM_CATEGORY) {
            require(expense.inventoryId != null) {
                "O item do estoque é obrigatório."
            }
            require(expense.quantity != null && expense.quantity > 0) {
                "Despesas de estoque devem possuir quantidade maior que zero."
            }
        } else {
            require(expense.inventoryId == null) {
                "O item do estoque é permitido apenas para despesas de estoque."
            }
            require(expense.quantity == null) {
                "A quantidade é permitida apenas para despesas de estoque."
            }
        }
    }

    private fun validateProducts(expense: Expense) {
        val products = expense.products
        val productIds = products.map { it.productId }

        when (expense.allocationType) {
            GENERAL -> {
                require(products.isEmpty()) {
                    "Despesas gerais não devem possuir produtos vinculados."
                }
                return
            }

            SPECIFIC_PRODUCT -> {
                require(products.size == 1) {
                    "A despesa deve possuir exatamente um produto vinculado."
                }
            }

            CATEGORY_PRODUCTS -> {
                require(products.size >= 2) {
                    "A despesa deve possuir pelo menos dois produtos vinculados."
                }
            }
        }

        require(productIds.distinct().size == productIds.size) {
            "Não é permitido vincular produtos duplicados."
        }

        require(
            productIds.all(
                productExpenseValidationOutputPort::existsById,
            ),
        ) {
            "Um ou mais produtos vinculados não existem."
        }

        if (expense.allocationType == CATEGORY_PRODUCTS) {
            require(
                productExpenseValidationOutputPort.belongToSameCategory(
                    productIds,
                ),
            ) {
                "Todos os produtos vinculados devem pertencer à mesma categoria."
            }
        }

        require(products.all { it.allocatedValue > ZERO }) {
            "O valor alocado para cada produto deve ser maior que zero."
        }

        val totalAllocated =
            products.fold(ZERO) { total, product ->
                total.add(product.allocatedValue)
            }

        require(totalAllocated.compareTo(expense.value) == 0) {
            "A soma dos valores alocados deve corresponder ao valor total da despesa."
        }
    }
}
