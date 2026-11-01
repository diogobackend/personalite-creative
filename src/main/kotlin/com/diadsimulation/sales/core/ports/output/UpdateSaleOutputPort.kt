package com.diadsimulation.sales.core.ports.output

import com.diadsimulation.sales.core.domain.model.Sale

interface UpdateSaleOutputPort {
    fun update(sale: Sale): Sale
}
