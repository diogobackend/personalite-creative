package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.CATEGORY_PRODUCTS
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.GENERAL
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.SPECIFIC_PRODUCT
import com.diadsimulation.expenses.core.domain.enums.ExpensePaymentType
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
import com.diadsimulation.movements.core.domain.enums.MovementDirection.OUT
import com.diadsimulation.movements.core.domain.enums.MovementOperation
import com.diadsimulation.movements.core.domain.enums.MovementOperation.CREATED
import com.diadsimulation.movements.core.domain.enums.MovementOrigin.EXPENSE
import com.diadsimulation.movements.core.domain.models.Movement
import com.diadsimulation.movements.core.ports.output.MovementRepositoryOutputPort
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal.ZERO
import java.time.OffsetDateTime
import java.util.UUID

open class CreateExpenseUseCaseImplementation(
    private val expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
    private val productExpenseValidationOutputPort: ProductExpenseValidationOutputPort,
    private val expenseStockOutputPort: ExpenseStockOutputPort,
    private val expenseCategoryOutputPort: ExpenseCategoryOutputPort,
    private val expensePaymentRepositoryOutputPort: ExpensePaymentRepositoryOutputPort,
    private val movementRepositoryOutputPort: MovementRepositoryOutputPort,
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

        val now = OffsetDateTime.now()

        movementRepositoryOutputPort.save(
            Movement(
                id = UUID.randomUUID(),
                originId = savedExpense.id,
                origin = EXPENSE,
                operation = CREATED,
                direction = null,
                status = savedExpense.status.name,
                previousValue = null,
                currentValue = savedExpense.value,
                amount = savedExpense.value,
                description = "Despesa registrada.",
                createdAt = now,
            ),
        )

        if (savedExpense.paidAmount > ZERO) {
            expensePaymentRepositoryOutputPort.save(
                ExpensePayment(
                    expenseId = savedExpense.id,
                    amount = savedExpense.paidAmount,
                    type = ExpensePaymentType.PAYMENT,
                    paidAt = now,
                ),
            )

            movementRepositoryOutputPort.save(
                Movement(
                    id = UUID.randomUUID(),
                    originId = savedExpense.id,
                    origin = EXPENSE,
                    operation = MovementOperation.PAYMENT,
                    direction = OUT,
                    status = savedExpense.status.name,
                    previousValue = ZERO,
                    currentValue = savedExpense.paidAmount,
                    amount = savedExpense.paidAmount,
                    description = "Pagamento inicial da despesa.",
                    createdAt = now,
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
