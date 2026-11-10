package com.diadsimulation.sales.app.adapter.output.persistence.adapters

import com.diadsimulation.sales.app.adapter.output.persistence.entity.SalePaymentEntity
import com.diadsimulation.sales.app.adapter.output.persistence.repository.SalePaymentJpaRepository
import com.diadsimulation.sales.core.domain.model.SalePayment
import com.diadsimulation.sales.core.ports.output.SalePaymentRepositoryOutputPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SalePaymentRepositoryAdapter(
    private val salePaymentJpaRepository: SalePaymentJpaRepository,
) : SalePaymentRepositoryOutputPort {
    override fun save(payment: SalePayment): SalePayment = salePaymentJpaRepository.save(payment.toEntity()).toDomain()

    override fun findBySaleId(saleId: UUID): List<SalePayment> =
        salePaymentJpaRepository
            .findAllBySaleIdOrderByPaidAtAsc(saleId)
            .map { it.toDomain() }

    private fun SalePayment.toEntity() =
        SalePaymentEntity(
            id = id,
            saleId = saleId,
            amount = amount,
            type = type,
            paidAt = paidAt,
            createdAt = createdAt,
        )

    private fun SalePaymentEntity.toDomain() =
        SalePayment(
            id = id,
            saleId = saleId,
            amount = amount,
            type = type,
            paidAt = paidAt,
            createdAt = createdAt,
        )
}
