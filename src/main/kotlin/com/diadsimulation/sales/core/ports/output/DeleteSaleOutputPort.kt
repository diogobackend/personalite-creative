package com.diadsimulation.sales.core.ports.output

import java.util.UUID

interface DeleteSaleOutputPort {
    fun deleteById(id: UUID)
}
