package com.anderson.placeholder.replacer.placeholder_replacer.inteface;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public interface PDFPlaceholderHandler {
    void replacePlaceholder(PDDocument document, String placeholder, String replacement, String outputPdfPath) throws IOException;
}
