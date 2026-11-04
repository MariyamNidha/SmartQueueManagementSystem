package com.smartqueue.repository;

import com.smartqueue.model.QueueStatus;
import com.smartqueue.model.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QueueTokenRepository extends JpaRepository<QueueToken, Long> {

    List<QueueToken> findByStatusOrderByQueuePositionAsc(QueueStatus status);

    QueueToken findTopByStatusOrderByQueuePositionAsc(QueueStatus status);

    List<QueueToken> findAllByOrderByCreatedAtDesc();

    long countByStatus(QueueStatus status);

    @Query("SELECT COALESCE(MAX(t.queuePosition), 0) FROM QueueToken t")
    Integer findMaxQueuePosition();
}
