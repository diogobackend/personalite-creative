package com.diadsimulation.sales.core.ports.output

import com.diadsimulation.sales.core.domain.model.Sale
import java.util.UUID

interface FindSaleByIdOutputPort {
    fun findById(id: UUID): Sale?
}
