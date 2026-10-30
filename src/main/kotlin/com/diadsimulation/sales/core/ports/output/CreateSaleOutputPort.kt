package com.diadsimulation.sales.core.ports.output

import com.diadsimulation.sales.core.domain.model.Sale

interface CreateSaleOutputPort {
    fun save(sale: Sale): Sale
}
