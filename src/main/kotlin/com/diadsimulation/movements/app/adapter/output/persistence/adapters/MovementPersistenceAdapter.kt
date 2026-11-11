package com.diadsimulation.movements.app.adapter.output.persistence.adapters

import com.diadsimulation.movements.app.adapter.output.persistence.mappers.MovementPersistenceMapper
import com.diadsimulation.movements.app.adapter.output.persistence.repositories.MovementJpaRepository
import com.diadsimulation.movements.app.adapter.output.persistence.specifications.MovementSpecifications
import com.diadsimulation.movements.core.domain.models.Movement
import com.diadsimulation.movements.core.domain.models.MovementFilter
import com.diadsimulation.movements.core.domain.models.MovementPage
import com.diadsimulation.movements.core.ports.output.MovementRepositoryOutputPort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class MovementPersistenceAdapter(
    private val repository: MovementJpaRepository,
    private val mapper: MovementPersistenceMapper,
) : MovementRepositoryOutputPort {
    override fun save(movement: Movement): Movement =
        repository
            .save(mapper.toEntity(movement))
            .let(mapper::toDomain)

    override fun findById(id: UUID): Movement? =
        repository
            .findById(id)
            .orElse(null)
            ?.let(mapper::toDomain)

    @Transactional(readOnly = true)
    override fun findAll(filter: MovementFilter): MovementPage {
        val direction = Sort.Direction.fromString(filter.sortDirection)

        val pageable =
            PageRequest.of(
                filter.page,
                filter.size,
                Sort.by(direction, filter.sortBy),
            )

        val result =
            repository.findAll(
                MovementSpecifications.withFilters(filter),
                pageable,
            )

        return MovementPage(
            content = result.content.map(mapper::toDomain),
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            first = result.isFirst,
            last = result.isLast,
            empty = result.isEmpty,
        )
    }
}
