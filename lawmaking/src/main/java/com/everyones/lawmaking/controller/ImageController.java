package com.everyones.lawmaking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ResourceLoader resourceLoader;

    @GetMapping("/v1/images/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource resource = resourceLoader.getResource("classpath:" + filename);
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        // Determine content type
        String contentType = getContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(resource);
    }

    private String getContentType(String filename) {
        String[] fileExtension = filename.split("\\.");
        String extension = fileExtension[fileExtension.length - 1].toLowerCase();

        switch (extension) {
            case "png":
                return MimeTypeUtils.IMAGE_PNG_VALUE;
            case "gif":
                return MimeTypeUtils.IMAGE_GIF_VALUE;
//            case "bmp":
//                return MimeTypeUtils.IMAGE_BMP_VALUE;
            default:
                return MimeTypeUtils.IMAGE_JPEG_VALUE;
        }
    }
}
