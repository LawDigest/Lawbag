package com.everyones.lawmaking.repository;


import com.everyones.lawmaking.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
            select n
            from Notification n
            Join User u on u.id = :userId
            where n.user.id = :userId
            """)
    List<Notification> findAllByUserId(@Param("userId") Long userId);


}
