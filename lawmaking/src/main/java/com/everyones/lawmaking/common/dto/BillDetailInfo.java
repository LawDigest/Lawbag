package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDetailInfo extends BillInfoDto{

    private String billLink;

    public BillDetailInfo(Bill bill) {
        super(bill);
        this.billLink = bill.getBillLink();
    }

    public static BillDetailInfo from(Bill bill) {
        return new BillDetailInfo(bill);
    }

}

