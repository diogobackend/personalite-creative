package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.CANCELLED
import com.diadsimulation.expenses.core.ports.input.DeleteExpenseInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseStockOutputPort
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.util.UUID

class DeleteExpenseUseCaseImplementation(
    private val expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
    private val expenseStockOutputPort: ExpenseStockOutputPort,
) : DeleteExpenseInputPort {
    @Transactional
    override fun execute(id: UUID) {
        val expense =
            expenseRepositoryOutputPort.findById(id)
                ?: throw NoSuchElementException("Despesa não encontrada para o ID: $id")

        if (expense.category == "item de estoque" &&
            expense.status != CANCELLED
        ) {
            expenseStockOutputPort.updateEntry(
                previousExpense = expense,
                updatedExpense =
                    expense.copy(
                        status = CANCELLED,
                    ),
                reason = "EXPENSE_DELETED",
            )
        }

        expenseRepositoryOutputPort.deleteById(id)
    }
}
