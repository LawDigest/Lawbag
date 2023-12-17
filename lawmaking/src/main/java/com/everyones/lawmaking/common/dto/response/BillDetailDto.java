package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.domain.entity.Bill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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

    private String representProposerParty; // 추가

    private Long representProposerPartyId; // 추가

    private List<String> publicProposerList;

    private List<String> publicProposerIdList;

    private List<Map<String, Object>> proposerPartyCountList = new ArrayList<>();

    private List<Map<String, Object>> proposerPartyIdList = new ArrayList<>();

    private int view = 0;

    private int like = 0;

    public BillDetailDto(String id, String name, String proposers, String gpt_summary, LocalDate proposeDate, String stage,
                         String representProposer, String representProposerId, String representProposerParty, Long representProposerPartyId) {
        this.id = id;
        this.name = name;
        this.gptSummary = gpt_summary;
        this.proposeDate = proposeDate;
        this.representProposer = representProposer;
        this.representProposerId = representProposerId;
        this.representProposerParty = representProposerParty;
        this.representProposerPartyId = representProposerPartyId;
        this.stage = stage;
        this.proposers = proposers;

        var proposerPartyCountMap = new ConcurrentHashMap<String, Object>();
        proposerPartyCountMap.put("name", representProposerParty);
        proposerPartyCountMap.put("count", 1);
        this.proposerPartyCountList.add(proposerPartyCountMap);

        var proposerPartyIdMap = new ConcurrentHashMap<String, Object>();
        proposerPartyIdMap.put("name", representProposerParty);
        proposerPartyIdMap.put("party_id", representProposerPartyId);
        this.proposerPartyIdList.add(proposerPartyIdMap);
    }
}
