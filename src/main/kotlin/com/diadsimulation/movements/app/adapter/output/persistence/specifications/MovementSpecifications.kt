package com.diadsimulation.movements.app.adapter.output.persistence.specifications

import com.diadsimulation.movements.app.adapter.output.persistence.entities.MovementEntity
import com.diadsimulation.movements.core.domain.models.MovementFilter
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object MovementSpecifications {
    fun withFilters(filter: MovementFilter): Specification<MovementEntity> =
        Specification { root, _, cb ->

            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

            filter.createdFrom?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        it,
                    )
            }

            filter.createdTo?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("createdAt"),
                        it,
                    )
            }

            filter.minAmount?.let {
                predicates +=
                    cb.greaterThanOrEqualTo(
                        root.get("amount"),
                        it,
                    )
            }

            filter.maxAmount?.let {
                predicates +=
                    cb.lessThanOrEqualTo(
                        root.get("amount"),
                        it,
                    )
            }

            filter.status?.takeIf { it.isNotBlank() }?.let {
                predicates +=
                    cb.equal(
                        cb.lower(root.get("status")),
                        it.lowercase(),
                    )
            }

            filter.origin?.let {
                predicates += cb.equal(root.get<Any>("origin"), it)
            }

            filter.direction?.let {
                predicates += cb.equal(root.get<Any>("direction"), it)
            }

            filter.operation?.let {
                predicates += cb.equal(root.get<Any>("operation"), it)
            }

            filter.description?.takeIf { it.isNotBlank() }?.let {
                predicates +=
                    cb.like(
                        cb.lower(root.get("description")),
                        "%${it.trim().lowercase()}%",
                    )
            }

            filter.originId?.trim()?.takeIf { it.isNotEmpty() }?.let { id ->
                val uuid = runCatching { UUID.fromString(id) }.getOrNull()

                if (uuid != null) {
                    predicates += cb.equal(root.get<UUID>("originId"), uuid)
                } else {
                    predicates +=
                        cb.like(
                            cb.lower(
                                root.get<UUID>("originId").cast(String::class.java),
                            ),
                            "${id.lowercase()}%",
                        )
                }
            }

            cb.and(*predicates.toTypedArray())
        }
}
