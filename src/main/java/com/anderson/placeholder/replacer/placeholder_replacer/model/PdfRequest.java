package com.anderson.placeholder.replacer.placeholder_replacer.model;

public class PdfRequest {
    private String placeholder;
    private String replacement;
    private String inputPdfPath;
    private String outputPdfPath;

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getInputPdfPath() {
        return inputPdfPath;
    }

    public void setInputPdfPath(String inputPdfPath) {
        this.inputPdfPath = inputPdfPath;
    }

    public String getOutputPdfPath() {
        return outputPdfPath;
    }

    public void setOutputPdfPath(String outputPdfPath) {
        this.outputPdfPath = outputPdfPath;
    }
}
