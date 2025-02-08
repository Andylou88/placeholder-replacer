package com.anderson.placeholder.replacer.placeholder_replacer.controller;

import com.anderson.placeholder.replacer.placeholder_replacer.inteface.PdfHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfController {

    private static final Logger LOGGER = Logger.getLogger(PdfController.class.getName());
    private final PdfHandler pdfHandler;

    public PdfController(PdfHandler pdfHandler) {
        this.pdfHandler = pdfHandler;
    }

    @PostMapping(value = "/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> replacePlaceholder(@RequestParam("pdfFile") MultipartFile pdfFile,
                                                     @RequestParam("placeholders") String placeholders,
                                                     @RequestParam("replacements") String replacements) throws IOException {
        LOGGER.info("Received request to replace placeholders in PDF");
        LOGGER.info("Creating temporary file");
        File tempFile = File.createTempFile("temp", ".pdf");
        pdfFile.transferTo(tempFile);

        LOGGER.info("Loading PDF document");
        try (var document = PDDocument.load(tempFile)) {
            pdfHandler.replacePlaceholder(document, placeholders, replacements);

            File outputFile = File.createTempFile("modified", ".pdf");
            document.save(outputFile);
            byte[] pdfBytes = Files.readAllBytes(outputFile.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IOException e) {
            LOGGER.severe("Error processing PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
