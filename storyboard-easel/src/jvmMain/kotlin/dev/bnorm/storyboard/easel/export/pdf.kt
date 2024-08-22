package dev.bnorm.storyboard.easel.export

import androidx.compose.ui.renderComposeScene
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.ui.PreviewSlide
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File

fun exportAsPdf(
    storyboard: Storyboard,
    file: File,
    width: Int = storyboard.size.width.value.toInt(),
    height: Int = storyboard.size.height.value.toInt(),
) {
    val doc = PDDocument()

    for ((page, frame) in storyboard.frames.withIndex()) {
        val image = renderComposeScene(width, height) {
            PreviewSlide(storyboard, frame)
        }

        createPage(image, page, doc, width, height)
    }

    doc.save(file)
    doc.close()
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
