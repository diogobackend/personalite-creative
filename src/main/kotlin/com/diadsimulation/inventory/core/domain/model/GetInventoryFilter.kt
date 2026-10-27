package com.diadsimulation.inventory.core.domain.model

import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.products.core.domain.enums.ProductCategory
import java.time.LocalDate

data class GetInventoryFilter(
    val sku: String? = null,
    val name: String? = null,
    val category: ProductCategory? = null,
    val description: String? = null,
    val quantity: Int? = null,
    val minQuantity: Int? = null,
    val maxQuantity: Int? = null,
    val unit: InventoryUnit? = null,
    val color: String? = null,
    val size: InventorySize? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val createdAt: LocalDate? = null,
    val createdFrom: LocalDate? = null,
    val createdTo: LocalDate? = null,
    val page: Int = 0,
    val pageSize: Int = 20,
    val sortBy: String = "name",
    val sortDirection: String = "ASC",
)
