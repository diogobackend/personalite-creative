package com.diadsimulation.reports.app.adapter.input.web.swagger

import com.diadsimulation.reports.app.adapter.input.web.dtos.response.ReportOverviewResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Tag(name = "Relatórios", description = "Relatórios financeiros")
interface ReportApi {
    @Operation(
        summary = "Consultar visão geral financeira",
        description = "Retorna os indicadores financeiros do período informado. Por padrão, utiliza o mês atual.",
    )
    fun getOverview(
        @Parameter(description = "Data inicial do período (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        startDate: LocalDate?,
        @Parameter(description = "Data final do período (yyyy-MM-dd)")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DATE)
        endDate: LocalDate?,
    ): ReportOverviewResponse
}
