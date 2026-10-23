package com.diadsimulation.expenses.core.usecase

import com.diadsimulation.expenses.core.domain.model.ExpensesPage
import com.diadsimulation.expenses.core.domain.model.GetExpensesFilter
import com.diadsimulation.expenses.core.ports.input.GetExpensesInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort

class GetExpensesUseCaseImplementation(
    private val expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
) : GetExpensesInputPort {
    override fun execute(filter: GetExpensesFilter): ExpensesPage = expenseRepositoryOutputPort.findAll(filter)
}
