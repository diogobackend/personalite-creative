package com.diadsimulation.sales.core.usecase

import com.diadsimulation.sales.core.ports.input.DeleteSaleInputPort
import com.diadsimulation.sales.core.ports.input.FindSaleByIdInputPort
import com.diadsimulation.sales.core.ports.output.DeleteSaleOutputPort
import java.util.UUID

class DeleteSaleUseCaseImplementation(
    private val findSaleByIdInputPort: FindSaleByIdInputPort,
    private val deleteSaleOutputPort: DeleteSaleOutputPort,
) : DeleteSaleInputPort {
    override fun execute(id: UUID) {
        findSaleByIdInputPort.execute(id)
        deleteSaleOutputPort.deleteById(id)
    }
}
