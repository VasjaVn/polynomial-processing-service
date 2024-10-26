package com.example.polynomial.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_PDF;

@Hidden
@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final ResourceLoader resourceLoader;

    @GetMapping(produces = "application/pdf")
    public ResponseEntity<Resource> getPdf() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/pdf/Polynomial_Processing_API.pdf");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Polynomial_Processing_API.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(APPLICATION_PDF)
                .body(resource);
    }
}
