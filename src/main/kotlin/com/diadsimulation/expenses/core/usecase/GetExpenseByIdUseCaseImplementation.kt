package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.ports.input.GetExpenseByIdInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import java.util.NoSuchElementException
import java.util.UUID

class GetExpenseByIdUseCaseImplementation(
    private val expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
) : GetExpenseByIdInputPort {
    override fun execute(id: UUID): Expense =
        expenseRepositoryOutputPort.findById(id)
            ?: throw NoSuchElementException(
                "Despesa não encontrada para o ID: $id",
            )
}
