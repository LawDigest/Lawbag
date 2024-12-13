package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.common.dto.response.BillListResponse;
import com.everyones.lawmaking.global.util.AuthenticationUtil;
import com.everyones.lawmaking.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BillFacade {
    private final CommonFacade commonFacade;
    private final BillService billService;

    /**
     * @param pageable
     * @return BillListResponse
     */
    @Transactional(readOnly = true)
    public BillListResponse getBillList(Pageable pageable) {
        return billService.getBillList(pageable);
    }
    public BillListResponse getBillList(Pageable pageable, String stage) {
        var billListResponse = billService.getBillList(pageable, stage);
        return setBillListResponseBookMark(billListResponse);
    }

    private BillListResponse setBillListResponseBookMark(BillListResponse billListResponse) {
        var userId = AuthenticationUtil.getUserId();

        if (userId.isEmpty()) {
            return billListResponse;
        }
        return billListResponse;
    }

}
