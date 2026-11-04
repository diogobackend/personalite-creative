package com.diadsimulation.categories.core.ports.input

import com.diadsimulation.categories.core.domain.model.Category
import java.util.UUID

interface UpdateCategoryInputPort {
    fun execute(
        id: UUID,
        name: String,
    ): Category
}
