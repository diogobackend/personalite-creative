package com.diadsimulation.categories.core.ports.input

import java.util.UUID

interface DeleteCategoryInputPort {
    fun execute(id: UUID)
}
