package com.diadsimulation.expenses.app.adapter.output.persistence.adapters

import com.diadsimulation.expenses.app.adapter.output.persistence.mappers.ExpensePersistenceMapper
import com.diadsimulation.expenses.app.adapter.output.persistence.repositories.ExpenseJpaRepository
import com.diadsimulation.expenses.app.adapter.output.persistence.specifications.ExpenseSpecifications
import com.diadsimulation.expenses.core.domain.model.Expense
import com.diadsimulation.expenses.core.domain.model.ExpensesPage
import com.diadsimulation.expenses.core.domain.model.GetExpensesFilter
import com.diadsimulation.expenses.core.ports.output.ExpenseRepositoryOutputPort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class ExpensePersistenceAdapter(
    private val expenseJpaRepository: ExpenseJpaRepository,
    private val expensePersistenceMapper: ExpensePersistenceMapper,
) : ExpenseRepositoryOutputPort {
    @Transactional
    override fun save(expense: Expense): Expense {
        val entity = expensePersistenceMapper.toEntity(expense)
        val savedEntity = expenseJpaRepository.save(entity)

        return expensePersistenceMapper.toDomain(savedEntity)
    }

    @Transactional(readOnly = true)
    override fun findById(id: UUID): Expense? =
        expenseJpaRepository
            .findById(id)
            .orElse(null)
            ?.let(expensePersistenceMapper::toDomain)

    @Transactional(readOnly = true)
    override fun findByProductId(productId: UUID): List<Expense> =
        expenseJpaRepository
            .findDistinctByProductsProductId(productId)
            .map(expensePersistenceMapper::toDomain)

    @Transactional(readOnly = true)
    override fun findAll(filter: GetExpensesFilter): ExpensesPage {
        val allowedSortFields =
            setOf(
                "name",
                "value",
                "category",
                "allocationType",
                "status",
                "quantity",
                "expenseDate",
                "createdAt",
                "updatedAt",
            )

        val sortBy =
            filter.sortBy.takeIf { it in allowedSortFields }
                ?: "expenseDate"

        val direction =
            if (filter.sortDirection.equals("DESC", ignoreCase = true)) {
                Sort.Direction.DESC
            } else {
                Sort.Direction.ASC
            }

        val pageable =
            PageRequest.of(
                filter.page.coerceAtLeast(0),
                filter.size.coerceIn(1, 100),
                Sort
                    .by(direction, sortBy)
                    .and(Sort.by("id")),
            )

        val specification = ExpenseSpecifications.withFilters(filter)

        val result = expenseJpaRepository.findAll(specification, pageable)

        return ExpensesPage(
            content = result.content.map(expensePersistenceMapper::toDomain),
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalElementsThisPage = result.numberOfElements,
            totalPages = result.totalPages,
        )
    }
}
