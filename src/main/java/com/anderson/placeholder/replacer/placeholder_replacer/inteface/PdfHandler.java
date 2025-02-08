package com.anderson.placeholder.replacer.placeholder_replacer.inteface;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.Map;

public interface PdfHandler {
    void replacePlaceholder(PDDocument document, String placeholders, String replacements) throws IOException;
}
