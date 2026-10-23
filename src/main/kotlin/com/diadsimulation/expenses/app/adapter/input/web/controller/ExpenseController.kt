package com.diadsimulation.expenses.app.adapter.input.web.controller

import com.diadsimulation.expenses.app.adapter.input.web.mappers.ExpenseWebMapper
import com.diadsimulation.expenses.app.adapter.input.web.requests.CreateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.requests.GetExpensesRequest
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseResponse
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpensesPageResponse
import com.diadsimulation.expenses.app.adapter.input.web.swagger.ExpenseApi
import com.diadsimulation.expenses.core.ports.input.CreateExpenseInputPort
import com.diadsimulation.expenses.core.ports.input.GetExpenseByIdInputPort
import com.diadsimulation.expenses.core.ports.input.GetExpensesInputPort
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val createExpenseInputPort: CreateExpenseInputPort,
    private val getExpensesInputPort: GetExpensesInputPort,
    private val expenseWebMapper: ExpenseWebMapper,
    private val getExpenseByIdInputPort: GetExpenseByIdInputPort,
) : ExpenseApi {
    @PostMapping
    override fun create(request: CreateExpenseRequest): ResponseEntity<ExpenseResponse> {
        val expense = expenseWebMapper.toDomain(request)
        val createdExpense = createExpenseInputPort.execute(expense)

        return ResponseEntity
            .status(CREATED)
            .body(expenseWebMapper.toResponse(createdExpense))
    }

    @GetMapping
    override fun findAll(
        @ModelAttribute request: GetExpensesRequest,
    ): ResponseEntity<ExpensesPageResponse> {
        val filter = expenseWebMapper.toFilter(request)
        val expensesPage = getExpensesInputPort.execute(filter)

        return ResponseEntity.ok(
            expenseWebMapper.toPageResponse(expensesPage),
        )
    }

    @GetMapping("/{id}")
    override fun findById(id: UUID): ResponseEntity<ExpenseResponse> =
        ResponseEntity.ok(
            expenseWebMapper.toResponse(
                getExpenseByIdInputPort.execute(id),
            ),
        )
}
