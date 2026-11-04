package com.smartqueue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SmartQueueManagementSystemApplicationTests {

    @Test
    void queueServiceShouldExist() {
        assertDoesNotThrow(() -> Class.forName("com.smartqueue.service.QueueService"));
    }
}
