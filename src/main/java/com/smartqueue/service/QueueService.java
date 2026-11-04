package com.smartqueue.service;

import com.smartqueue.model.QueueStatus;
import com.smartqueue.model.QueueToken;
import com.smartqueue.repository.QueueTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class QueueService {

    private final QueueTokenRepository queueTokenRepository;

    public QueueService(QueueTokenRepository queueTokenRepository) {
        this.queueTokenRepository = queueTokenRepository;
    }

    public QueueToken generateToken(String customerName, String serviceType) {
        String cleanedCustomerName = StringUtils.hasText(customerName) ? customerName.trim() : "Walk-in Customer";
        String cleanedServiceType = StringUtils.hasText(serviceType) ? serviceType.trim() : "General";

        Integer maxQueuePosition = queueTokenRepository.findMaxQueuePosition();
        int nextPosition = (maxQueuePosition == null ? 0 : maxQueuePosition) + 1;

        String prefix = cleanedServiceType.substring(0, 1).toUpperCase();
        String tokenNumber = prefix + "-" + String.format("%03d", nextPosition);

        QueueToken token = new QueueToken(
            tokenNumber,
            cleanedCustomerName,
            cleanedServiceType,
            QueueStatus.WAITING,
            nextPosition,
            new Date()
        );

        return queueTokenRepository.save(token);
    }

    public List<QueueToken> getWaitingTokens() {
        return queueTokenRepository.findByStatusOrderByQueuePositionAsc(QueueStatus.WAITING);
    }

    public QueueToken getCurrentToken() {
        return queueTokenRepository.findTopByStatusOrderByQueuePositionAsc(QueueStatus.WAITING);
    }

    public QueueToken serveNextToken() {
        QueueToken nextToken = queueTokenRepository.findTopByStatusOrderByQueuePositionAsc(QueueStatus.WAITING);
        if (nextToken == null) {
            return null;
        }

        nextToken.setStatus(QueueStatus.SERVED);
        nextToken.setServedAt(new Date());
        return queueTokenRepository.save(nextToken);
    }

    public List<QueueToken> getHistory() {
        return queueTokenRepository.findAllByOrderByCreatedAtDesc();
    }

    public long countAllTokens() {
        return queueTokenRepository.count();
    }

    public long countWaitingTokens() {
        return queueTokenRepository.countByStatus(QueueStatus.WAITING);
    }

    public long countServedTokens() {
        return queueTokenRepository.countByStatus(QueueStatus.SERVED);
    }
}
