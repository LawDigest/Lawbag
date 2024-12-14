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
    private final BillService billService;

    @Transactional(readOnly = true)
    public BillListResponse getBillList(Pageable pageable, String stage) {
        return billService.getBillList(pageable, stage);
    }

}
