package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.ports.input.DeleteExpenseInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import java.util.UUID

class DeleteExpenseUseCaseImplementation(
    private val expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
) : DeleteExpenseInputPort {
    override fun execute(id: UUID) {
        expenseRepositoryOutputPort.deleteById(id)
    }
}
