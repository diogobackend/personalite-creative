package com.diadsimulation.categories.core.domain.model

import com.diadsimulation.categories.core.domain.enums.CategoryType
import java.time.LocalDateTime
import java.util.UUID

data class Category(
    val id: UUID,
    val name: String,
    val type: CategoryType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
