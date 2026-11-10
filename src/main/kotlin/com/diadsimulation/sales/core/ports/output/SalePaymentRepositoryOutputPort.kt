package com.diadsimulation.sales.core.ports.output

import com.diadsimulation.sales.core.domain.model.SalePayment
import java.util.UUID

interface SalePaymentRepositoryOutputPort {
    fun save(payment: SalePayment): SalePayment

    fun findBySaleId(saleId: UUID): List<SalePayment>
}
