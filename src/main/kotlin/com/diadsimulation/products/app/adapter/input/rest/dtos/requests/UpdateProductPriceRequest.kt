package com.diadsimulation.products.app.adapter.input.rest.dtos.requests

import com.fasterxml.jackson.databind.JsonNode
import java.util.UUID

data class UpdateProductPriceRequest(
    val id: UUID,
    val minimumQuantity: JsonNode? = null,
    val unitPrice: JsonNode? = null,
)
