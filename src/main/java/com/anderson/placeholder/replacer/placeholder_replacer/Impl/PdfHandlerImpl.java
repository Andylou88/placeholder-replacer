package com.anderson.placeholder.replacer.placeholder_replacer.Impl;

import com.anderson.placeholder.replacer.placeholder_replacer.inteface.PdfHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfHandlerImpl implements PdfHandler {

    private final ObjectMapper objectMapper;

    // Inject ObjectMapper via constructor
    public PdfHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void replacePlaceholder(PDDocument document, String placeholdersJson, String replacementsJson) throws IOException {
        var pdfStripper = new PDFTextStripper();
        String content = pdfStripper.getText(document);


        Map<String, String> placeholders = new HashMap<>();
        String[] placeholdersArray = objectMapper.readValue(placeholdersJson, String[].class);
        String[] replacementsArray = objectMapper.readValue(replacementsJson, String[].class);

        for (int i = 0; i < placeholdersArray.length; i++) {
            placeholders.put(placeholdersArray[i], replacementsArray[i]);
        }

        //replace all the placeholders
        for(Map.Entry<String,String> placeholder: placeholders.entrySet())
        {
            content = content.replaceAll(placeholder.getKey(), placeholder.getValue());
        }

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

                if (line.length() < 64) {
                    yPosition -= 15;
                }
                contentStream.endText();
                yPosition -= 15; // Move to the next line
            }
        }
    }
}