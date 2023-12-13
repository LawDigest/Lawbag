package com.everyones.lawmaking.controller;

import com.everyones.lawmaking.common.dto.CongressmanDto;
import com.everyones.lawmaking.service.CongressmanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//의원 자세히 보기 api
//특정 의원이 대표 발의 법안 api
//특정 의원이 공동 발의 법안 api
@RequiredArgsConstructor
@Controller
@Tag(name="Congressman Controller", description = "의원 상세 페이지")
public class CongressmanController {

}
