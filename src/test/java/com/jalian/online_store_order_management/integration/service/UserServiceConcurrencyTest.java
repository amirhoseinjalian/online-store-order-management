package com.jalian.online_store_order_management.integration.service;

import com.jalian.online_store_order_management.constant.BalanceOperation;
import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.dto.UpdateBalanceDto;
import com.jalian.online_store_order_management.exception.IllegalBalanceException;
import com.jalian.online_store_order_management.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceConcurrencyTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userRepository;

    private Long userId;
    private final long initialBalance = 1000;

    @BeforeEach
    public void setup() {
        var testUser = new User();
        testUser.setFirstName("Jalian");
        testUser.setLastName("Test");
        testUser.setEmail("jaliantest@gmail.com");
        testUser.setPassword("password");
        testUser.setUsername("jaliantest");
        testUser.setBalance(initialBalance);
        userId = userRepository.save(testUser).getId();
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteById(userId);
    }

    @Test
    public void testUpdateBalanceConcurrency() throws InterruptedException {
        int threadCount = 100;
        double delta = 10;
        double expectedBalance = initialBalance + (threadCount * delta);
        var latch = new CountDownLatch(threadCount);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        var dto = new UpdateBalanceDto(userId, delta, BalanceOperation.PLUS);
                        userService.updateBalance(dto);
                    } catch (IllegalBalanceException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        var user = userService.findUserEntityById(userId);
        assertEquals(expectedBalance, user.getBalance(), "Balance has not been updated correctly in concurrent addition.");
    }

    @Test
    public void testUpdateBalanceConcurrencyMixedOperations() throws InterruptedException {
        int depositThreads = 50;
        int withdrawalThreads = 50;
        int totalThreads = depositThreads + withdrawalThreads;
        double depositDelta = 10;
        double withdrawalDelta = 5;
        double expectedBalance = initialBalance + (depositThreads * depositDelta) - (withdrawalThreads * withdrawalDelta);
        var latch = new CountDownLatch(totalThreads);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < depositThreads; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep((long) (Math.random() * 10));
                        var dto = new UpdateBalanceDto(userId, depositDelta, BalanceOperation.PLUS);
                        userService.updateBalance(dto);
                    } catch (IllegalBalanceException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            for (int i = 0; i < withdrawalThreads; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep((long) (Math.random() * 10));
                        var dto = new UpdateBalanceDto(userId, withdrawalDelta, BalanceOperation.MINUS);
                        userService.updateBalance(dto);
                    } catch (IllegalBalanceException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        }

        var user = userService.findUserEntityById(userId);
        assertEquals(expectedBalance, user.getBalance(), "Balance has not been updated correctly for mixed operations.");
    }

    @Test
    public void testUpdateBalanceIllegalWithdrawal() throws InterruptedException {
        int threadCount = 20;
        double withdrawalAmount = 100;
        var latch = new CountDownLatch(threadCount);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        var dto = new UpdateBalanceDto(userId, withdrawalAmount, BalanceOperation.MINUS);
                        userService.updateBalance(dto);
                    } catch (IllegalBalanceException e) {
                        // expected
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        var user = userService.findUserEntityById(userId);
        assertTrue(user.getBalance() >= 0, "Balance should not be negative after illegal withdrawals.");
    }

    @Test
    public void testUpdateBalanceConcurrencyRepeated() throws InterruptedException {
        int iterations = 10;
        for (int j = 0; j < iterations; j++) {
            resetUserBalance();
            testUpdateBalanceConcurrencyIteration();
        }
    }

    private void testUpdateBalanceConcurrencyIteration() throws InterruptedException {
        int threadCount = 100;
        double delta = 10;
        double expectedBalance = initialBalance + (threadCount * delta);
        var latch = new CountDownLatch(threadCount);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        var dto = new UpdateBalanceDto(userId, delta, BalanceOperation.PLUS);
                        userService.updateBalance(dto);
                    } catch (IllegalBalanceException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        var user = userService.findUserEntityById(userId);
        Assertions.assertEquals(expectedBalance, user.getBalance(), "Iteration failed: Balance has not been updated correctly.");
    }

    private void resetUserBalance() {
        var user = userRepository.findById(userId).orElseThrow();
        user.setBalance(initialBalance);
        userRepository.save(user);
    }
}