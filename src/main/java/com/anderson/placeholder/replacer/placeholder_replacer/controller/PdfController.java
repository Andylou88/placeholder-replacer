package com.anderson.placeholder.replacer.placeholder_replacer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @GetMapping("/replace")
    public ResponseEntity<String> replacePlaceholder()
    {
        return ResponseEntity.ok("PDF updated successfully");
    }

}
