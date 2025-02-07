package com.anderson.placeholder.replacer.placeholder_replacer.model;

import java.util.Map;

public class PdfRequest {
    private Map<String, String> placeholders;
    private String inputPdfPath;
    private String outputPdfPath;


    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public String getInputPdfPath() {
        return inputPdfPath;
    }

    public String getOutputPdfPath() {
        return outputPdfPath;
    }
}
