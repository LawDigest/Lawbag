package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


//패키지 내부에 파일이 없으면 디렉토리 구조를 못올리기 때문에 생성한 겸 예시 파일입니다.
@RestController
@RequiredArgsConstructor
public class HelloController {
    private final HelloService helloService;

    @GetMapping("/hello")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> hello() {
        return helloService.hello();
    }
}
