package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BillDetailDto {
    private String id;
    private String name;
    private String gptSummary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate proposeDate;

    private String stage;

    private String proposers;

    private String representProposer;

    private String representProposerId;

    private List<String> publicProposerList;

    private List<String> publicProposerIdList;

    private int view = 0;

    private int like = 0;

    public BillDetailDto(String id, String name, String proposers, String gpt_summary, LocalDate proposeDate, String stage,
                         String representProposer, String representProposerId) {
        this.id = id;
        this.name = name;
        this.gptSummary = gpt_summary;
        this.proposeDate = proposeDate;
        this.representProposer = representProposer;
        this.representProposerId = representProposerId;
        this.stage = stage;
        this.proposers = proposers;
    }
}
