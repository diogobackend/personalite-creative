package com.diadsimulation.expenses.app.adapter.config

import com.diadsimulation.expenses.core.ports.input.CreateExpenseInputPort
import com.diadsimulation.expenses.core.ports.input.GetExpensesInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ProductExpenseValidationOutputPort
import com.diadsimulation.expenses.core.usecase.CreateExpenseUseCaseImplementation
import com.diadsimulation.expenses.core.usecase.GetExpensesUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExpenseUseCaseConfiguration {
    @Bean
    fun createExpenseInputPort(
        expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
        productExpenseValidationOutputPort: ProductExpenseValidationOutputPort,
    ): CreateExpenseInputPort =
        CreateExpenseUseCaseImplementation(
            expenseRepositoryOutputPort = expenseRepositoryOutputPort,
            productExpenseValidationOutputPort = productExpenseValidationOutputPort,
        )

    @Bean
    fun getExpensesInputPort(expenseRepositoryOutputPort: ExpenseRepositoryOutputPort): GetExpensesInputPort =
        GetExpensesUseCaseImplementation(
            expenseRepositoryOutputPort = expenseRepositoryOutputPort,
        )
}
