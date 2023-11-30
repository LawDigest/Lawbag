package com.everyones.lawmaking.service;

import com.everyones.lawmaking.domain.entity.Hello;
import com.everyones.lawmaking.repository.HelloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//패키지 내부에 파일이 없으면 디렉토리 구조를 못올리기 때문에 생성한 겸 예시 파일입니다.
@Service
@RequiredArgsConstructor
public class HelloService {
    private final HelloRepository helloRepository;

    public ResponseEntity<String>  hello() {
        var helloMsg = Hello.builder()
                .hello("Hello World")
                .build();
        var saved = helloRepository.save(helloMsg);
        return ResponseEntity.ok("msg: %s, Created - %s".formatted(saved, LocalDateTime.now()));
    }

}
