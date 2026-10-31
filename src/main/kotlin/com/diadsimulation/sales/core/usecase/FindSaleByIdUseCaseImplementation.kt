package com.diadsimulation.sales.core.usecase

import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.ports.input.FindSaleByIdInputPort
import com.diadsimulation.sales.core.ports.output.FindSaleByIdOutputPort
import java.util.NoSuchElementException
import java.util.UUID

class FindSaleByIdUseCaseImplementation(
    private val findSaleByIdOutputPort: FindSaleByIdOutputPort,
) : FindSaleByIdInputPort {
    override fun execute(id: UUID): Sale =
        findSaleByIdOutputPort.findById(id)
            ?: throw NoSuchElementException(
                "Venda não encontrada para o ID: $id",
            )
}
