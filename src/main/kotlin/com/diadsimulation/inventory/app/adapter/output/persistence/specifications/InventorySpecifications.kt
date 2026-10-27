package com.diadsimulation.inventory.app.adapter.output.persistence.specifications

import com.diadsimulation.inventory.app.adapter.output.persistence.entity.InventoryEntity
import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.inventory.core.domain.model.GetInventoryFilter
import com.diadsimulation.products.core.domain.enums.ProductCategory
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

object InventorySpecifications {
    fun withFilters(filter: GetInventoryFilter): Specification<InventoryEntity> =
        Specification { root, _, cb ->
            if (filter.sku != null) {
                return@Specification cb.equal(
                    root.get<String>("sku"),
                    filter.sku,
                )
            }

            val predicates = mutableListOf<Predicate>()

            fun contains(
                field: String,
                value: String?,
            ) {
                value
                    ?.takeIf(String::isNotBlank)
                    ?.let {
                        predicates +=
                            cb.like(
                                cb.lower(root.get(field)),
                                "%${it.trim().lowercase()}%",
                            )
                    }
            }

            contains("name", filter.name)
            contains("description", filter.description)
            contains("color", filter.color)
            contains("material", filter.material)
            contains("brand", filter.brand)
            contains("model", filter.model)

            filter.category?.let {
                predicates += cb.equal(root.get<ProductCategory>("category"), it)
            }

            filter.unit?.let {
                predicates += cb.equal(root.get<InventoryUnit>("unit"), it)
            }

            filter.size?.let {
                predicates += cb.equal(root.get<InventorySize>("size"), it)
            }

            filter.quantity?.let {
                predicates += cb.equal(root.get<Int>("quantity"), it)
            }

            filter.minQuantity?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("quantity"),
                        it,
                    )
            }

            filter.maxQuantity?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("quantity"),
                        it,
                    )
            }

            fun startOfDay(date: LocalDate): OffsetDateTime = date.atStartOfDay().atOffset(ZoneOffset.UTC)

            filter.createdAt?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        startOfDay(it),
                    )

                predicates +=
                    cb.lessThan(
                        root.get("createdAt"),
                        startOfDay(it.plusDays(1)),
                    )
            }

            filter.createdFrom?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        startOfDay(it),
                    )
            }

            filter.createdTo?.let {
                predicates +=
                    cb.lessThan(
                        root.get("createdAt"),
                        startOfDay(it.plusDays(1)),
                    )
            }

            cb.and(*predicates.toTypedArray())
        }
}
