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

        // extract text from pdf document
        var pdfStripper = new PDFTextStripper();
        String content = pdfStripper.getText(document);

        //convert json strings to arrays and insert into map
        Map<String, String> placeholders = new HashMap<>();
        String[] placeholdersArray = objectMapper.readValue(placeholdersJson, String[].class);
        String[] replacementsArray = objectMapper.readValue(replacementsJson, String[].class);

        for (int i = 0; i < placeholdersArray.length; i++) {
            placeholders.put(placeholdersArray[i], replacementsArray[i]);
        }

        //replace all the placeholders
        for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            content = content.replaceAll(placeholder.getKey(), placeholder.getValue());
        }

        // Split content into lines
        String[] lines = content.split("\\r?\\n");

        PDPage page = document.getPage(0);
        PDRectangle mediaBox = page.getMediaBox();
        try (var contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, false)) {
            // Write the updated content back to the PDF
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            float margin = 72; // Margin from the left side
            float yPosition = mediaBox.getUpperRightY() - margin;  // Start position
            float pageWidth = mediaBox.getWidth() - 2 * margin;
            String wordsOutsideMargins = "";
            int linecount = 0;

            for (String line : lines) {
                linecount++;

                // appending words outside the margins in front of the next line
                // unless text is part of closing signature
                if (!wordsOutsideMargins.isBlank()) {
                    if (linecount >= 16) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(wordsOutsideMargins);
                        contentStream.endText();
                        yPosition -= 15;
                    } else {
                        line = wordsOutsideMargins + line;
                        wordsOutsideMargins = "";
                    }
                }

                // Rewrites the text back to the pdf document
                // while formatting the text to stay within the margins
                String[] words = line.split(" ");
                StringBuilder lineBuilder = new StringBuilder();
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    String tempLine = lineBuilder.toString() + word + " ";
                    float tempLineWidth = PDType1Font.TIMES_ROMAN.getStringWidth(tempLine) / 1000 * 12;
                    if (tempLineWidth < pageWidth) {
                        lineBuilder.append(word).append(" ");
                    } else {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(lineBuilder.toString().trim());
                        contentStream.endText();
                        lineBuilder.setLength(0);
                        lineBuilder.append(word).append(" ");
                        yPosition -= 15; // Move to the next line
                    }

                    // Check if end of sentence was reached and adds the words that were
                    // beyond the page width to wordsOutsideMargins
                    if (i == words.length - 1 && linecount > 1) {
                        wordsOutsideMargins = lineBuilder.toString();
                    }

                    // adds a new lines after the greeting and before the end signature
                    if (linecount == 1 && i == words.length - 1) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(lineBuilder.toString().trim());

                        if (line.length() < 64) {
                            yPosition -= 15;
                        }

                        contentStream.endText();
                        yPosition -= 15;
                    } else if (linecount > 15 && i == words.length - 1) {
                        yPosition -= 15;
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(lineBuilder.toString().trim());
                        contentStream.endText();
                    }
                }
            }
        }
    }
}