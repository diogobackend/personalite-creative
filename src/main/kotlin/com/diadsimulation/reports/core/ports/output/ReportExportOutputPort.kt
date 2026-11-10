package com.diadsimulation.reports.core.ports.output

import com.diadsimulation.reports.core.domain.enums.ReportExportFormat
import com.diadsimulation.reports.core.domain.enums.ReportExportType
import java.time.LocalDate

interface ReportExportOutputPort {
    fun export(
        startDate: LocalDate,
        endDate: LocalDate,
        format: ReportExportFormat,
        type: ReportExportType,
    ): ByteArray
}
