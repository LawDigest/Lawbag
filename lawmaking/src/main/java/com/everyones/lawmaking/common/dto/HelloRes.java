package com.everyones.lawmaking.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HelloRes {
    private long helloId;
    private String hello;
}
