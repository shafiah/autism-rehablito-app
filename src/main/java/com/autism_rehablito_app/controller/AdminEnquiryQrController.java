package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.enquiry.AdminEnquiryQrConfigResponse;
import com.autism_rehablito_app.service.EnquiryQrService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/enquiry-qr")
public class AdminEnquiryQrController {

    private final EnquiryQrService enquiryQrService;

    public AdminEnquiryQrController(EnquiryQrService enquiryQrService) {
        this.enquiryQrService = enquiryQrService;
    }

    @GetMapping("/config")
    public ResponseEntity<AdminEnquiryQrConfigResponse> getEnquiryQrConfig() {

        boolean enabled = enquiryQrService.isEnquiryQrEnabled();

        String publicFormUrl = null;

        if (enabled) {
            publicFormUrl = enquiryQrService.getPublicFormUrl();
        }

        AdminEnquiryQrConfigResponse response =
                new AdminEnquiryQrConfigResponse(enabled, publicFormUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateEnquiryQrImage(
            @RequestParam(defaultValue = "400") int width,
            @RequestParam(defaultValue = "400") int height) {

        byte[] qrImage = enquiryQrService.generateEnquiryQrCode(width, height);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=enquiry-qr.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }
}