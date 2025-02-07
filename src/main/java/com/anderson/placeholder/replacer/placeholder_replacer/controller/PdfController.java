package com.anderson.placeholder.replacer.placeholder_replacer.controller;

import com.anderson.placeholder.replacer.placeholder_replacer.model.PdfRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @GetMapping("/replace")
    public ResponseEntity<String> replacePlaceholder(@RequestBody PdfRequest request)
    {
        try (var document = PDDocument.load(new File(request.getInputPdfPath()))) {
            replacePlaceholder(document, request.getPlaceholder(), request.getReplacement(), request.getOutputPdfPath());return ResponseEntity.ok("PDF updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating PDF: " + e.getMessage());
        }
    }

    private static void replacePlaceholder(PDDocument document, String placeholder, String replacement, String outputPdfPath) throws IOException {
        var pdfStripper = new PDFTextStripper();
        String content = pdfStripper.getText(document);

        //replace all the placeholders
        content = content.replaceAll(placeholder,replacement);

        // Split content into lines
        String[] lines = content.split("\\r?\\n");

        PDPage page = document.getPage(0);
        PDRectangle mediaBox = page.getMediaBox();
        try (var contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, false)) {
            // Write the updated content back to the PDF
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            float margin = 60; // Margin from the left side
            float yPosition = mediaBox.getUpperRightY() - margin;  // Start position

            for (String line: lines) {
                contentStream.beginText();
                contentStream.newLineAtOffset(mediaBox.getLowerLeftX() + margin, yPosition); // Align text to the left with a margin
                contentStream.showText(line);
                contentStream.endText();
                yPosition -= 15; // Move to the next line
            }
        }

        document.save(outputPdfPath);
    }

}
