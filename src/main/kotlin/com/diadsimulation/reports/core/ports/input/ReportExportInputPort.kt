package com.diadsimulation.reports.core.ports.input

import com.diadsimulation.reports.core.domain.enums.ReportExportFormat
import com.diadsimulation.reports.core.domain.enums.ReportExportType
import java.time.LocalDate

interface ReportExportInputPort {
    fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
        format: ReportExportFormat,
        type: ReportExportType,
    ): ByteArray
}
