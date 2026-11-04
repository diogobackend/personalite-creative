package com.diadsimulation.categories.app.adapter.input.web.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateCategoryRequest(
    @field:NotBlank
    @field:Size(max = 150)
    val name: String,
)
