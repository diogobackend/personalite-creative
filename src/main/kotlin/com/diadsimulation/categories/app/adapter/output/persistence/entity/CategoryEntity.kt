package com.diadsimulation.categories.app.adapter.output.persistence.entity

import com.diadsimulation.categories.core.domain.enums.CategoryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "categories")
class CategoryEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,
    @Column(name = "name", nullable = false, length = 150)
    val name: String,
    @Enumerated(STRING)
    @Column(name = "type", nullable = false, length = 30)
    val type: CategoryType,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,
)
