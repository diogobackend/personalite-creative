package com.diadsimulation.sales.app.adapter.input.web.mappers

import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SaleFinancialSummaryResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SaleItemSummaryResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SalePaginationResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SaleSummaryResponse
import com.diadsimulation.sales.app.adapter.input.web.dtos.responses.SearchSalesResponse
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.DUE_TODAY
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.NO_DEADLINE
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.ON_TIME
import com.diadsimulation.sales.core.domain.enums.SaleDeadlineStatus.OVERDUE
import com.diadsimulation.sales.core.domain.enums.SalePaymentStatus
import com.diadsimulation.sales.core.domain.model.Sale
import com.diadsimulation.sales.core.domain.model.SaleFinancialSummary
import com.diadsimulation.sales.core.domain.model.SaleItem
import com.diadsimulation.sales.core.ports.output.SaleSearchResult
import com.diadsimulation.sales.core.usecase.SaleFinancialCalculator
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate

@Component
class SearchSalesWebMapper {
    fun toResponse(
        result: SaleSearchResult,
        today: LocalDate = LocalDate.now(),
    ): SearchSalesResponse =
        SearchSalesResponse(
            sales = result.sales.map { toSaleResponse(it, today) },
            pagination =
                SalePaginationResponse(
                    page = result.page,
                    size = result.size,
                    totalElements = result.totalElements,
                    totalPages = result.totalPages,
                    hasNext = result.page + 1 < result.totalPages,
                    hasPrevious = result.page > 0,
                ),
            summary = toSummaryResponse(result.summary),
        )

    private fun toSaleResponse(
        sale: Sale,
        today: LocalDate,
    ): SaleSummaryResponse =
        SaleSummaryResponse(
            id = sale.id,
            customerId = sale.customerId,
            deliveryDeadline = sale.deliveryDeadline,
            totalAmount = sale.totalAmount,
            downPaymentPercentage = sale.downPaymentPercentage,
            downPaymentAmount =
                SaleFinancialCalculator.downPaymentAmount(
                    sale.totalAmount,
                    sale.downPaymentPercentage,
                ),
            paidPercentage = sale.paidPercentage,
            paidAmount =
                SaleFinancialCalculator.paidAmount(
                    sale.totalAmount,
                    sale.paidPercentage,
                ),
            remainingPercentage =
                SaleFinancialCalculator.remainingPercentage(
                    sale.paidPercentage,
                ),
            remainingAmount =
                SaleFinancialCalculator.remainingAmount(
                    sale.totalAmount,
                    sale.paidPercentage,
                ),
            paymentMethod = sale.paymentMethod,
            paymentStatus = paymentStatus(sale.paidPercentage),
            deadlineStatus = deadlineStatus(sale.deliveryDeadline, today),
            observation = sale.observation,
            items = sale.items.map(::toItemResponse),
            createdAt = sale.createdAt,
            updatedAt = sale.updatedAt,
        )

    private fun toItemResponse(item: SaleItem): SaleItemSummaryResponse =
        SaleItemSummaryResponse(
            id = item.id,
            productId = item.productId,
            inventoryId = item.inventoryId,
            quantity = item.quantity,
            color = item.color,
            brand = item.brand,
            model = item.model,
            size = item.size,
            material = item.material,
        )

    private fun toSummaryResponse(summary: SaleFinancialSummary): SaleFinancialSummaryResponse =
        SaleFinancialSummaryResponse(
            totalSales = summary.totalSales,
            totalAmount = summary.totalAmount,
            totalDownPayment = summary.totalDownPayment,
            totalReceived = summary.totalReceived,
            totalRemaining = summary.totalRemaining,
            unpaidSales = summary.unpaidSales,
            partiallyPaidSales = summary.partiallyPaidSales,
            paidSales = summary.paidSales,
            overdueSales = summary.overdueSales,
            dueTodaySales = summary.dueTodaySales,
            upcomingSales = summary.upcomingSales,
        )

    private fun paymentStatus(paidPercentage: BigDecimal): SalePaymentStatus =
        when {
            paidPercentage.compareTo(ZERO) == 0 -> {
                SalePaymentStatus.UNPAID
            }

            paidPercentage.compareTo(HUNDRED) >= 0 -> {
                SalePaymentStatus.PAID
            }

            else -> {
                SalePaymentStatus.PARTIALLY_PAID
            }
        }

    private fun deadlineStatus(
        deadline: LocalDate?,
        today: LocalDate,
    ): SaleDeadlineStatus =
        when {
            deadline == null -> {
                NO_DEADLINE
            }

            deadline.isBefore(today) -> {
                OVERDUE
            }

            deadline.isEqual(today) -> {
                DUE_TODAY
            }

            else -> {
                ON_TIME
            }
        }

    companion object {
        private val HUNDRED = BigDecimal("100")
    }
}
