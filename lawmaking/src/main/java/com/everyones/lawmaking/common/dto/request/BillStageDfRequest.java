package com.everyones.lawmaking.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillStageDfRequest {
    private int age;

    private Long billNumber;

    private String stage;

    private String committee;
}
