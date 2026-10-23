package com.diadsimulation.expenses.app.adapter.output.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "expense_products")
class ExpenseProductEntity(
    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(
        name = "expense_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_expense_products_expense"),
    )
    val expense: ExpenseEntity,
    @Column(name = "product_id", nullable = false)
    val productId: UUID,
    @Column(
        name = "allocated_value",
        nullable = false,
        precision = 15,
        scale = 2,
    )
    val allocatedValue: BigDecimal,
)
