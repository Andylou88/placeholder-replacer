package com.anderson.placeholder.replacer.placeholder_replacer.controller;

import com.anderson.placeholder.replacer.placeholder_replacer.Impl.PDFPlaceholderHandlerImpl;
import com.anderson.placeholder.replacer.placeholder_replacer.inteface.PDFPlaceholderHandler;
import com.anderson.placeholder.replacer.placeholder_replacer.model.PdfRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    PDFPlaceholderHandler placeholderHandler = new PDFPlaceholderHandlerImpl();

    @PostMapping("/replace")
    public ResponseEntity<String> replacePlaceholder(@RequestBody PdfRequest request)
    {
        try (var document = PDDocument.load(new File(request.getInputPdfPath()))) {
            placeholderHandler.replacePlaceholder(document, request.getPlaceholders(), request.getOutputPdfPath());
            return ResponseEntity.ok("PDF updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating PDF: " + e.getMessage());
        }
    }

}
