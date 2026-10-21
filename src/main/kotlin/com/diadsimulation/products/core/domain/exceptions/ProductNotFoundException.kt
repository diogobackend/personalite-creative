package com.diadsimulation.products.core.domain.exceptions

import java.util.UUID

class ProductNotFoundException(
    productId: UUID,
) : RuntimeException("Produto não encontrado: $productId")
