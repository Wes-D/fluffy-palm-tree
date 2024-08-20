package com.example.charapp

import android.graphics.pdf.PdfDocument
import android.graphics.Canvas
import android.graphics.Paint
import android.content.Context
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.util.Log
import java.io.IOException
import android.net.Uri
import android.widget.Toast
import java.io.OutputStream

fun generateCharacterPdf(context: Context, characterViewModel: CharacterViewModel, uri: Uri) {
    // Create a new PdfDocument
    val pdfDocument = PdfDocument()

    // Create a page description (specify the page size)
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size (595x842 pixels)
    val page = pdfDocument.startPage(pageInfo)

    // Set up canvas and paint for writing text to the PDF
    val canvas: Canvas = page.canvas
    val paint = Paint()

    // Write the character details onto the PDF
    paint.textSize = 18f
    canvas.drawText("Character Name: ${characterViewModel.characterName}", 80f, 100f, paint)
    canvas.drawText("Race: ${characterViewModel.race}", 80f, 130f, paint)
    canvas.drawText("Archetype: ${characterViewModel.archetype}", 80f, 160f, paint)

    // Write stats, traits, and abilities
    canvas.drawText("Stats:", 80f, 200f, paint)
    var yPosition = 230f
    characterViewModel.stats.forEach { (stat, value) ->
        canvas.drawText("$stat: $value", 80f, yPosition, paint)
        yPosition += 30f
    }

    yPosition += 30f
    canvas.drawText("Traits:", 80f, yPosition, paint)
    yPosition += 30f
    characterViewModel.traits.forEach { trait ->
        canvas.drawText(trait, 80f, yPosition, paint)
        yPosition += 30f
    }

    yPosition += 30f
    canvas.drawText("Abilities:", 80f, yPosition, paint)
    yPosition += 30f
    characterViewModel.abilities.forEach { ability ->
        canvas.drawText(ability, 80f, yPosition, paint)
        yPosition += 30f
    }

    // Finish the page
    pdfDocument.finishPage(page)

    // Write the PDF to the selected location
    try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }
        Toast.makeText(context, "PDF saved successfully", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to save PDF: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        pdfDocument.close()
    }
}
