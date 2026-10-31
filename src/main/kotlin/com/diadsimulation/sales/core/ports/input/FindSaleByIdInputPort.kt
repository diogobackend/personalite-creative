package com.diadsimulation.sales.core.ports.input

import com.diadsimulation.sales.core.domain.model.Sale
import java.util.UUID

interface FindSaleByIdInputPort {
    fun execute(id: UUID): Sale
}
