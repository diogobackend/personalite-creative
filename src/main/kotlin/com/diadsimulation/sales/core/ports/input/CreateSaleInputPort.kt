package com.diadsimulation.sales.core.ports.input

import com.diadsimulation.sales.core.domain.model.Sale

interface CreateSaleInputPort {
    fun execute(sale: Sale): Sale
}
