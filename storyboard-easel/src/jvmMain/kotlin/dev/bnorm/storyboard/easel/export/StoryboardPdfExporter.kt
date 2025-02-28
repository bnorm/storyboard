package dev.bnorm.storyboard.easel.export

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.renderComposeScene
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.ui.SlidePreview
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

class StoryboardPdfExporter(
    private val storyboard: Storyboard,
) {
    var status by mutableStateOf<ExportStatus?>(null)
        private set

    suspend fun export(
        width: Int = storyboard.size.width.value.toInt(),
        height: Int = storyboard.size.height.value.toInt(),
    ) {
        val file = FileKit.saveFile(
            bytes = null,
            baseName = storyboard.title,
            extension = "pdf",
            initialDirectory = null,
            platformSettings = null,
        )
        if (file != null) {
            withContext(Dispatchers.IO) {
                runInterruptible {
                    exportAsPdf(
                        path = file.file.toPath(),
                        width = width,
                        height = height,
                    )
                }
            }
        }
    }

    private fun exportAsPdf(path: Path, width: Int, height: Int) {
        try {
            val doc = PDDocument()

            val frames = storyboard.frames
            for ((page, frame) in frames.withIndex()) {
                status = ExportStatus(page.toFloat() / frames.size, "Generating PDF...")
                val image = renderComposeScene(width, height) {
                    SlidePreview(storyboard, frame)
                }

                createPage(image, page, doc, width, height)
            }

            val bytes = ByteArrayOutputStream()
            doc.save(bytes)
            doc.close()

            status = ExportStatus(1.0f, "Saving PDF...")
            path.writeBytes(
                array = bytes.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        } finally {
            status = null
        }
    }

    private fun createPage(
        image: Image,
        index: Int,
        doc: PDDocument,
        width: Int,
        height: Int,
    ) {
        val bytes = image.encodeToData(EncodedImageFormat.PNG)?.bytes
        val name = "slide-${index.toString().padStart(3, '0')}"

        val page = PDPage(PDRectangle(width.toFloat(), height.toFloat()))
        doc.addPage(page)

        val contentStream = PDPageContentStream(doc, page)
        contentStream.drawImage(PDImageXObject.createFromByteArray(doc, bytes, name), 0f, 0f)
        contentStream.close()
    }
}
