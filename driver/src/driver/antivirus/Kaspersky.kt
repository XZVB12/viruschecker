package driver.antivirus

import driver.config.AntivirusType
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Kaspersky(
    scanCommand: ExecutableCommand,
    updateCommand: ExecutableCommand
) : UpdatableCommandLineAntivirus(scanCommand, updateCommand) {

    private val logger = KotlinLogging.logger { }

    override val type: AntivirusType = AntivirusType.KASPERSKY

    override fun parseReportFile(
        reportFile: File,
        params: FileScanParameters
    ): Sequence<ReportEntry> {
        val linesWithScannedFile = Files.readAllLines(reportFile.toPath().toAbsolutePath())
            .asSequence()
            .filterNot { it.startsWith(";") }
            .filter { params.fileToScan.name in it }
        return linesWithScannedFile
            .map { line ->
                line.split("\t").let {
                    ReportEntry(
                        datetime = LocalDateTime.parse(
                            it[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        ),
                        status = ReportEntry.Status.fromCommonName(it[2].trim()),
                        description = if (it.size > 3) it[3].trim() else ""
                    )
                }
            }
    }
}