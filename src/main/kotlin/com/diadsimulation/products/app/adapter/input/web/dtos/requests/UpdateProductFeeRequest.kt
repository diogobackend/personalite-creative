package com.diadsimulation.products.app.adapter.input.web.dtos.requests

import com.fasterxml.jackson.databind.JsonNode
import java.util.UUID

data class UpdateProductFeeRequest(
    val id: UUID,
    val name: JsonNode? = null,
    val amount: JsonNode? = null,
    val chargeType: JsonNode? = null,
    val required: JsonNode? = null,
    val active: JsonNode? = null,
)
