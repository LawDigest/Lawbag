package com.everyones.lawmaking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


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
        /**
         TODO
         컨텐츠 타입 헤더 설정하는 메서드가 포함되어있는데, 우리가 구현한 BaseResponse에도 적용하려면 어떻게 해야할지 고민
         다른 프로젝트에서는 굳이 ContentType을 설정해주지 않긴 헀는데, 설정 안하면 예외 상황 발생할 것 같음.
         어차피 이미지 관련 리팩토링할 때 제대로 로직 변경하면 될 듯
        */
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
