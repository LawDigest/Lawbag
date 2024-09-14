package com.everyones.lawmaking.common.dto.response;

import com.everyones.lawmaking.common.dto.BillTimelineDto;
import com.everyones.lawmaking.domain.entity.BillTimeline;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillTimelineResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date;

    Integer billCount;

    List<BillTimelineDto> billTimelineList;

    public static BillTimelineResponse of(LocalDate proposeDate, List<BillTimeline> billTimelines) {
        var billTimeLineList = billTimelines.stream()
                .map(BillTimelineDto::from)
                .toList();
        return BillTimelineResponse.builder()
                .date(proposeDate)
                .billCount(billTimelines.size())
                .billTimelineList(billTimeLineList)
                .build();
    }

}
