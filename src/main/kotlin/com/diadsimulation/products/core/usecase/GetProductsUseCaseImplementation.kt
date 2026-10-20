package com.diadsimulation.products.core.usecase

import com.diadsimulation.products.core.domain.model.GetProductsFilter
import com.diadsimulation.products.core.domain.model.ProductsPage
import com.diadsimulation.products.core.port.input.GetProductsInputPort
import com.diadsimulation.products.core.port.output.ProductRepositoryOutputPort

class GetProductsUseCaseImplementation(
    private val productRepositoryOutputPort: ProductRepositoryOutputPort,
) : GetProductsInputPort {
    override fun execute(filter: GetProductsFilter): ProductsPage {
        val effectiveFilter =
            if (filter.code != null) {
                GetProductsFilter(
                    code = filter.code,
                    page = 0,
                    size = 1,
                )
            } else {
                filter.copy(
                    page = filter.page.coerceAtLeast(0),
                    size = filter.size.coerceIn(1, 100),
                )
            }

        return productRepositoryOutputPort.findAll(effectiveFilter)
    }
}
