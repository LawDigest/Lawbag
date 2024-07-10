package com.everyones.lawmaking.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillResultDfRequest {
    private int age;

    private Long billNumber;

    private String billProposeResult;
}
