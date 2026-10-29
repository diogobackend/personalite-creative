package com.diadsimulation.expenses.app.adapter.output.persistence.entities

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "expenses")
class ExpenseEntity(
    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),
    @Column(name = "inventory_id")
    var inventoryId: UUID? = null,
    @Column(nullable = false, length = 150)
    var name: String,
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    @Column(nullable = false, precision = 15, scale = 2)
    var value: BigDecimal,
    @Enumerated(STRING)
    @Column(nullable = false, length = 30)
    var category: ExpenseCategory,
    @Enumerated(STRING)
    @Column(name = "allocation_type", nullable = false, length = 30)
    var allocationType: ExpenseAllocationType,
    @Enumerated(STRING)
    @Column(nullable = false, length = 20)
    var status: ExpenseStatus,
    @Column
    var quantity: Int? = null,
    @Column(name = "expense_date", nullable = false)
    var expenseDate: LocalDate,
    @OneToMany(
        mappedBy = "expense",
        cascade = [ALL],
        orphanRemoval = true,
    )
    val products: MutableList<ExpenseProductEntity> = mutableListOf(),
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
