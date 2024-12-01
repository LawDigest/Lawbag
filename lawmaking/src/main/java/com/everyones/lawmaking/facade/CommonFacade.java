package com.everyones.lawmaking.facade;

import com.everyones.lawmaking.service.DataService;
import com.everyones.lawmaking.service.LikeService;
import com.everyones.lawmaking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonFacade {
    private final NotificationService notificationService;
    private final DataService dataService;
    private final LikeService likeService;

}
