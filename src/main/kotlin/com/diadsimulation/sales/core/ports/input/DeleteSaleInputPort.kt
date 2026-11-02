package com.diadsimulation.sales.core.ports.input

import java.util.UUID

interface DeleteSaleInputPort {
    fun execute(id: UUID)
}
