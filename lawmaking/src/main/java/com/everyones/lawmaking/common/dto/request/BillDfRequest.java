package com.everyones.lawmaking.common.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillDfRequest {
    private String billId;

    private int billNumber;

    private String billName;

    private String stage;

    private String billResult;

    private int assemblyNumber;

    private String summary;

    private String gptSummary;

    private String briefSummary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate proposeDate;

    private String proposers;

    private List<String> publicProposerIdList;

    private List<String> rstProposerIdList;

    private String proposerKind;


}
