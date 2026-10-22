package com.diadsimulation.products.app.adapter.input.web.dtos.requests

import com.fasterxml.jackson.databind.JsonNode

data class UpdateProductRequest(
    val name: JsonNode? = null,
    val category: JsonNode? = null,
    val description: JsonNode? = null,
    val minimumQuantity: JsonNode? = null,
    val pricingType: JsonNode? = null,
    val shelfLifeDays: JsonNode? = null,
    val productionLeadTimeMinDays: JsonNode? = null,
    val productionLeadTimeMaxDays: JsonNode? = null,
    val active: JsonNode? = null,
    val prices: JsonNode? = null,
    val fees: JsonNode? = null,
    val addPrices: JsonNode? = null,
    val removePriceIds: JsonNode? = null,
    val addFees: JsonNode? = null,
    val removeFeeIds: JsonNode? = null,
)
