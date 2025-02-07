package com.anderson.placeholder.replacer.placeholder_replacer.model;

public class PdfRequest {
    private String placeholder;
    private String replacement;
    private String inputPdfPath;
    private String outputPdfPath;

    public String getPlaceholder() {
        return placeholder;
    }

    public String getReplacement() {
        return replacement;
    }

    public String getInputPdfPath() {
        return inputPdfPath;
    }

    public String getOutputPdfPath() {
        return outputPdfPath;
    }
}
