package com.diadsimulation.categories.app.adapter.input.web.dtos.responses

import com.diadsimulation.categories.core.domain.enums.CategoryType
import java.time.LocalDateTime
import java.util.UUID

data class CategoryResponse(
    val id: UUID,
    val name: String,
    val type: CategoryType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
