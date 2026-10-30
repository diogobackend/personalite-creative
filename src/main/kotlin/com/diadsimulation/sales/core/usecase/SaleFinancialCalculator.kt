package com.diadsimulation.sales.core.usecase

import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP

object SaleFinancialCalculator {
    private val HUNDRED = BigDecimal("100")
    private const val MONETARY_SCALE = 2

    fun downPaymentAmount(
        totalAmount: BigDecimal,
        downPaymentPercentage: BigDecimal,
    ): BigDecimal = percentageAmount(totalAmount, downPaymentPercentage)

    fun paidAmount(
        totalAmount: BigDecimal,
        paidPercentage: BigDecimal,
    ): BigDecimal = percentageAmount(totalAmount, paidPercentage)

    fun remainingAmount(
        totalAmount: BigDecimal,
        paidPercentage: BigDecimal,
    ): BigDecimal =
        totalAmount
            .subtract(paidAmount(totalAmount, paidPercentage))
            .setScale(MONETARY_SCALE, HALF_UP)

    fun remainingPercentage(paidPercentage: BigDecimal): BigDecimal = HUNDRED.subtract(paidPercentage)

    private fun percentageAmount(
        totalAmount: BigDecimal,
        percentage: BigDecimal,
    ): BigDecimal =
        totalAmount
            .multiply(percentage)
            .divide(HUNDRED, MONETARY_SCALE, HALF_UP)
}
