package dev.bnorm.storyboard.easel.export

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.renderComposeScene
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.ScenePreview
import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.writeBytes

class StoryboardPdfExporter {
    var status by mutableStateOf<ExportStatus?>(null)
        private set

    suspend fun export(
        storyboard: Storyboard,
    ) {
        val file = FileKit.saveFile(
            bytes = null,
            baseName = storyboard.title,
            extension = "pdf",
            initialDirectory = null,
            platformSettings = null,
        )
        if (file != null) {
            exportAsPdf(
                storyboard = storyboard,
                path = file.file.toPath(),
            )
        }
    }

    private suspend fun exportAsPdf(
        storyboard: Storyboard,
        path: Path,
    ) {
        try {
            val doc = PDDocument()

            val frames = storyboard.indices
            for ((page, frame) in frames.withIndex()) {
                status = ExportStatus(page.toFloat() / frames.size, "Generating PDF...")
                val image = renderComposeScene(
                    width = storyboard.format.size.width,
                    height = storyboard.format.size.height,
                    // Not needed because preview sets the density, but good for consistency.
                    density = storyboard.format.density,
                ) {
                    ScenePreview(storyboard, frame)
                }

                withContext(Dispatchers.IO) {
                    runInterruptible {
                        createPage(image, page, doc)
                    }
                }
            }

            val bytes = ByteArrayOutputStream()
            withContext(Dispatchers.IO) {
                runInterruptible {
                    doc.save(bytes)
                    doc.close()
                }
            }

            status = ExportStatus(1.0f, "Saving PDF...")
            withContext(Dispatchers.IO) {
                runInterruptible {
                    path.writeBytes(
                        array = bytes.toByteArray(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING
                    )
                }
            }
        } finally {
            status = null
        }
    }

    private fun createPage(image: Image, index: Int, doc: PDDocument) {
        val bytes = image.encodeToData(EncodedImageFormat.PNG)?.bytes
        val name = "frame-${index.toString().padStart(3, '0')}"

        val page = PDPage(PDRectangle(image.width.toFloat(), image.height.toFloat()))
        doc.addPage(page)

        val contentStream = PDPageContentStream(doc, page)
        contentStream.drawImage(PDImageXObject.createFromByteArray(doc, bytes, name), 0f, 0f)
        contentStream.close()
    }
}
