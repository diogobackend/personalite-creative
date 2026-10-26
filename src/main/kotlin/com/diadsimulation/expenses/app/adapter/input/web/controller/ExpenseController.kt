package com.diadsimulation.expenses.app.adapter.input.web.controller

import com.diadsimulation.expenses.app.adapter.input.web.mappers.ExpenseWebMapper
import com.diadsimulation.expenses.app.adapter.input.web.requests.CreateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.requests.GetExpensesRequest
import com.diadsimulation.expenses.app.adapter.input.web.requests.UpdateExpenseRequest
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpenseResponse
import com.diadsimulation.expenses.app.adapter.input.web.responses.ExpensesPageResponse
import com.diadsimulation.expenses.app.adapter.input.web.swagger.ExpenseApi
import com.diadsimulation.expenses.core.ports.input.CreateExpenseInputPort
import com.diadsimulation.expenses.core.ports.input.GetExpenseByIdInputPort
import com.diadsimulation.expenses.core.ports.input.GetExpensesInputPort
import com.diadsimulation.expenses.core.ports.input.UpdateExpenseInputPort
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.Validator
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
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
    private val updateExpenseInputPort: UpdateExpenseInputPort,
    private val objectMapper: ObjectMapper,
    private val validator: Validator,
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

    @PatchMapping("/{id}")
    override fun update(
        id: UUID,
        request: JsonNode,
    ): ResponseEntity<ExpenseResponse> {
        require(request.isObject) {
            "O corpo da requisição deve ser um objeto JSON."
        }

        val allowedFields =
            setOf(
                "name",
                "description",
                "value",
                "category",
                "allocationType",
                "status",
                "quantity",
                "products",
                "expenseDate",
            )

        val fieldsPresent = request.fieldNames().asSequence().toSet()

        require(fieldsPresent.all { it in allowedFields }) {
            "A requisição contém campos não permitidos."
        }

        val updateRequest =
            objectMapper.treeToValue(
                request,
                UpdateExpenseRequest::class.java,
            )

        val violations = validator.validate(updateRequest)

        require(violations.isEmpty()) {
            violations.first().message
        }

        val update =
            expenseWebMapper.toUpdate(
                request = updateRequest,
                fieldsPresent = fieldsPresent,
            )

        val updatedExpense =
            updateExpenseInputPort.execute(
                id = id,
                update = update,
            )

        return ResponseEntity.ok(
            expenseWebMapper.toResponse(updatedExpense),
        )
    }
}
