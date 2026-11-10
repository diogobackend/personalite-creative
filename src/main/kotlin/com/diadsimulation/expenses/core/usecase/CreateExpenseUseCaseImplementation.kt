package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.CATEGORY_PRODUCTS
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.GENERAL
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.SPECIFIC_PRODUCT
import com.diadsimulation.expenses.core.domain.enums.ExpensePaymentType.PAYMENT
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PAID
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PARTIALLY_PAID
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PENDING
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpensePayment
import com.diadsimulation.expenses.core.ports.input.CreateExpenseInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseCategoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ExpensePaymentRepositoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseStockOutputPort
import com.diadsimulation.expenses.core.ports.output.ProductExpenseValidationOutputPort
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime

open class CreateExpenseUseCaseImplementation(
    private val expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
    private val productExpenseValidationOutputPort: ProductExpenseValidationOutputPort,
    private val expenseStockOutputPort: ExpenseStockOutputPort,
    private val expenseCategoryOutputPort: ExpenseCategoryOutputPort,
    private val expensePaymentRepositoryOutputPort: ExpensePaymentRepositoryOutputPort,
) : CreateExpenseInputPort {
    object Constants {
        const val INVENTORY_ITEM_CATEGORY = "item de estoque"
    }

    @Transactional
    override fun execute(expense: Expense): Expense {
        val categoryId =
            requireNotNull(expense.categoryId) {
                "O ID da categoria é obrigatório."
            }

        require(expenseCategoryOutputPort.existsByIdAndTypeExpense(categoryId)) {
            "A categoria informada não existe ou não pertence ao tipo EXPENSE."
        }

        validate(expense)

        val status =
            when {
                expense.paidAmount.compareTo(ZERO) == 0 -> PENDING
                expense.paidAmount.compareTo(expense.value) == 0 -> PAID
                else -> PARTIALLY_PAID
            }

        val savedExpense =
            expenseRepositoryOutputPort.save(
                expense.copy(status = status),
            )

        if (savedExpense.category == Constants.INVENTORY_ITEM_CATEGORY) {
            expenseStockOutputPort.registerEntry(savedExpense)
        }

        if (savedExpense.paidAmount > ZERO) {
            expensePaymentRepositoryOutputPort.save(
                ExpensePayment(
                    expenseId = savedExpense.id,
                    amount = savedExpense.paidAmount,
                    type = PAYMENT,
                    paidAt = OffsetDateTime.now(),
                ),
            )
        }

        return savedExpense
    }

    private fun validate(expense: Expense) {
        require(expense.name.isNotBlank()) {
            "O nome da despesa é obrigatório."
        }

        require(expense.value > ZERO) {
            "O valor da despesa deve ser maior que zero."
        }

        require(expense.paidAmount >= ZERO && expense.paidAmount <= expense.value) {
            "O valor pago deve estar entre zero e o valor total da despesa."
        }

        validateStock(expense)
        validateProducts(expense)
    }

    private fun validateStock(expense: Expense) {
        if (expense.category == Constants.INVENTORY_ITEM_CATEGORY) {
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

        require(productIds.all(productExpenseValidationOutputPort::existsById)) {
            "Um ou mais produtos vinculados não existem."
        }

        if (expense.allocationType == CATEGORY_PRODUCTS) {
            require(productExpenseValidationOutputPort.belongToSameCategory(productIds)) {
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
