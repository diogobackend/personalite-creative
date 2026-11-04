package com.diadsimulation.categories.app.adapter.input.web.dtos.requests

import com.diadsimulation.categories.core.domain.enums.CategoryType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCategoryRequest(
    @field:NotBlank
    @field:Size(max = 150)
    val name: String,
    val type: CategoryType,
)
