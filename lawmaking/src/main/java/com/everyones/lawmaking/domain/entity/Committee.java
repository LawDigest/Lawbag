package com.everyones.lawmaking.domain.entity;

import com.everyones.lawmaking.common.dto.request.CommitteeDfRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Committee extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "committee_id")
    private long id;

    @Column(name = "committee_div_code")
    private String committeeDivCode;

    @Column(name = "committee_div_name")
    private String committeeDivName;

    @Column(name = "committee_dept_code")
    private String committeeDeptCode;

    @Column(name = "committee_name")
    private String committeeName;

    @Column(name = "chairman_name")
    private String chairmanName;

    @OneToMany(mappedBy = "committee")
    private List<CommitteeCongressman> committeeMembers = new ArrayList<>();

    @OneToMany(mappedBy = "committee")
    private List<Bill> billList = new ArrayList<>();

    @Column(name = "member_limit_count")
    private int memberLimitCount;

    @Column(name = "member_count")
    private int committeeMemberCount;

    @Column(name = "non_negotiating_member_cnt")
    private int nonNegotiatingMemberCount;

    @Column(name = "negotiating_member_cnt")
    private int negotiatingMemberCount;

    // 실제 테스트용으로만 강제 ID 설정
    public void setId(Long id) {
        this.id = id;
    }

    public static Committee create(CommitteeDfRequest committeeDfRequest) {


        return Committee.builder()
                .committeeDivCode(committeeDfRequest.getCommitteeDivCode())
                .committeeDivName(committeeDfRequest.getCommitteeDivName())
                .committeeDeptCode(committeeDfRequest.getCommitteeDeptCode())
                .committeeName(committeeDfRequest.getCommitteeName())
                .chairmanName(committeeDfRequest.getChairmanName())
                .memberLimitCount(committeeDfRequest.getMemberLimitCount())
                .committeeMemberCount(committeeDfRequest.getCommitteeMemberCount())
                .nonNegotiatingMemberCount(committeeDfRequest.getNonNegotiatingMemberCount())
                .negotiatingMemberCount(committeeDfRequest.getNegotiatingMemberCount())
                .build();
    }
    public void update(CommitteeDfRequest committeeDfRequest) {
        this.committeeDivCode = committeeDfRequest.getCommitteeDivCode();
        this.committeeDivName = committeeDfRequest.getCommitteeDivName();
        this.committeeDeptCode = committeeDfRequest.getCommitteeDeptCode();
        this.committeeName = committeeDfRequest.getCommitteeName();
        this.chairmanName = committeeDfRequest.getChairmanName();
        this.memberLimitCount = committeeDfRequest.getMemberLimitCount();
        this.committeeMemberCount = committeeDfRequest.getCommitteeMemberCount();
        this.nonNegotiatingMemberCount = committeeDfRequest.getNonNegotiatingMemberCount();
        this.negotiatingMemberCount = committeeDfRequest.getNegotiatingMemberCount();
    }

}
