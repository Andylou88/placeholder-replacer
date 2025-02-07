package com.anderson.placeholder.replacer.placeholder_replacer.inteface;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.Map;

public interface PDFPlaceholderHandler {
    void replacePlaceholder(PDDocument document, Map<String,String> placeholders, String outputPdfPath) throws IOException;
}
