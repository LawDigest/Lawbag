package com.everyones.lawmaking.common.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BillDfRequest {
    private String billId;

    private int billNumber;

    private String billName;

    private String stage;

    private int assemblyNumber;

    private String summary;

    private String gptSummary;

    private String briefSummary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate proposeDate;

    private String proposers;

    private List<String> publicProposers;

    private List<String> rstProposerNameList;

    private List<String> rstProposerPartyNameList;

    private String proposerKind;


}
