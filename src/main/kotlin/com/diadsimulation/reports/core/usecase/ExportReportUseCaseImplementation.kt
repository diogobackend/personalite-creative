package com.diadsimulation.reports.core.usecase

import com.diadsimulation.reports.core.domain.enums.ReportExportFormat
import com.diadsimulation.reports.core.domain.enums.ReportExportType
import com.diadsimulation.reports.core.ports.input.ReportExportInputPort
import com.diadsimulation.reports.core.ports.output.ReportExportOutputPort
import java.time.LocalDate

class ExportReportUseCaseImplementation(
    private val reportExportOutputPort: ReportExportOutputPort,
) : ReportExportInputPort {
    override fun execute(
        startDate: LocalDate,
        endDate: LocalDate,
        format: ReportExportFormat,
        type: ReportExportType,
    ): ByteArray {
        require(!startDate.isAfter(endDate)) {
            "A data inicial não pode ser posterior à data final."
        }

        return reportExportOutputPort.export(
            startDate = startDate,
            endDate = endDate,
            format = format,
            type = type,
        )
    }
}
