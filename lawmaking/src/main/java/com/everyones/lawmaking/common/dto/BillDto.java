package com.everyones.lawmaking.common.dto;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BillDto {
    private String billId;
    private String billName;
    private String representProposer;
    private String representProposerId;
    private String proposers;
    private String summary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate proposeDate;
    private int view;
    private int like;

    public BillDto(String billId, String name, String representProposer, String representProposerId, String proposers, String summary, LocalDate proposeDate) {
        this.billId = billId;
        this.billName = name;
        this.representProposer = representProposer;
        this.representProposerId = representProposerId;
        this.proposers = proposers;
        this.summary = summary;
        this.proposeDate = proposeDate;
        this.view = 0;
        this.like = 0;
    }



}
