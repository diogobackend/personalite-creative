package com.diadsimulation.sales.core.ports.input

import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.domain.model.SaleUpdate
import java.util.UUID

interface UpdateSaleInputPort {
    fun execute(
        id: UUID,
        update: SaleUpdate,
    ): Sale
}
