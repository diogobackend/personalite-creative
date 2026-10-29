package com.diadsimulation.expenses.app.config

import com.diadsimulation.expenses.core.ports.input.CreateExpenseInputPort
import com.diadsimulation.expenses.core.ports.input.DeleteExpenseInputPort
import com.diadsimulation.expenses.core.ports.input.GetExpenseByIdInputPort
import com.diadsimulation.expenses.core.ports.input.GetExpensesInputPort
import com.diadsimulation.expenses.core.ports.input.UpdateExpenseInputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import com.diadsimulation.expenses.core.ports.output.ExpenseStockOutputPort
import com.diadsimulation.expenses.core.ports.output.ProductExpenseValidationOutputPort
import com.diadsimulation.expenses.core.usecase.CreateExpenseUseCaseImplementation
import com.diadsimulation.expenses.core.usecase.DeleteExpenseUseCaseImplementation
import com.diadsimulation.expenses.core.usecase.GetExpenseByIdUseCaseImplementation
import com.diadsimulation.expenses.core.usecase.GetExpensesUseCaseImplementation
import com.diadsimulation.expenses.core.usecase.UpdateExpenseUseCaseImplementation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExpenseUseCaseConfiguration {
    @Bean
    fun createExpenseInputPort(
        expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
        productExpenseValidationOutputPort: ProductExpenseValidationOutputPort,
        expenseStockOutputPort: ExpenseStockOutputPort,
    ): CreateExpenseInputPort =
        CreateExpenseUseCaseImplementation(
            expenseRepositoryOutputPort = expenseRepositoryOutputPort,
            productExpenseValidationOutputPort = productExpenseValidationOutputPort,
            expenseStockOutputPort = expenseStockOutputPort,
        )

    @Bean
    fun getExpensesInputPort(expenseRepositoryOutputPort: ExpenseRepositoryOutputPort): GetExpensesInputPort =
        GetExpensesUseCaseImplementation(
            expenseRepositoryOutputPort = expenseRepositoryOutputPort,
        )

    @Bean
    fun getExpenseByIdInputPort(expenseRepositoryOutputPort: ExpenseRepositoryOutputPort): GetExpenseByIdInputPort =
        GetExpenseByIdUseCaseImplementation(expenseRepositoryOutputPort)

    @Bean
    fun updateExpenseInputPort(
        expenseRepositoryOutputPort: ExpenseRepositoryOutputPort,
        productExpenseValidationOutputPort: ProductExpenseValidationOutputPort,
        expenseStockOutputPort: ExpenseStockOutputPort,
    ): UpdateExpenseInputPort =
        UpdateExpenseUseCaseImplementation(
            expenseRepositoryOutputPort = expenseRepositoryOutputPort,
            productExpenseValidationOutputPort = productExpenseValidationOutputPort,
            expenseStockOutputPort = expenseStockOutputPort,
        )

    @Bean
    fun deleteExpenseInputPort(expenseRepositoryOutputPort: ExpenseRepositoryOutputPort): DeleteExpenseInputPort =
        DeleteExpenseUseCaseImplementation(
            expenseRepositoryOutputPort = expenseRepositoryOutputPort,
        )
}
