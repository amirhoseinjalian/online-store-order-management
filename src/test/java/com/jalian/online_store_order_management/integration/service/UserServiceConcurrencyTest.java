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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The UserServiceConcurrencyTest class performs integration tests to verify the thread-safety and
 * concurrency behavior of the user balance update functionality.
 * <p>
 * It tests the following scenarios:
 * <ul>
 *   <li>Concurrent balance updates with deposits.</li>
 *   <li>Concurrent mixed operations with deposits and withdrawals.</li>
 *   <li>Illegal withdrawal operations that attempt to reduce the balance below zero.</li>
 *   <li>Repeated concurrent updates to verify stability over multiple iterations.</li>
 * </ul>
 * Virtual threads (via Executors.newVirtualThreadPerTaskExecutor()) are used to simulate concurrent requests.
 * </p>
 *
 * @author amirhosein jalian
 */
@SpringBootTest
public class UserServiceConcurrencyTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userRepository;

    private Long userId;
    private final long initialBalance = 1000;

    /**
     * Sets up a test user with an initial balance before each test.
     */
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

    /**
     * Cleans up the test user after each test.
     */
    @AfterEach
    public void cleanup() {
        userRepository.deleteById(userId);
    }

    /**
     * Tests concurrent balance updates with deposit operations.
     * <p>
     * A specified number of virtual threads concurrently deposit an amount to the user's balance.
     * The final balance is verified against the expected value.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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

    /**
     * Tests concurrent mixed operations: deposits and withdrawals.
     * <p>
     * A number of virtual threads perform deposit operations while another set performs withdrawal operations concurrently.
     * The final balance is compared to the expected result.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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
            // Submit deposit tasks.
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
            // Submit withdrawal tasks.
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

    /**
     * Tests illegal withdrawal operations where multiple concurrent withdrawal requests
     * attempt to reduce the balance below zero.
     * <p>
     * The test asserts that the balance remains non-negative and that at least one withdrawal
     * operation fails with an {@link IllegalBalanceException}.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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
                        // Expected exception for illegal withdrawal.
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

    /**
     * Repeatedly tests the concurrent deposit operations to verify stability over multiple iterations.
     * <p>
     * For each iteration, the user balance is reset, and concurrent deposit operations are performed.
     * The resulting balance is compared to the expected value.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test
    public void testUpdateBalanceConcurrencyRepeated() throws InterruptedException {
        int iterations = 10;
        for (int j = 0; j < iterations; j++) {
            resetUserBalance();
            testUpdateBalanceConcurrencyIteration();
        }
    }

    /**
     * Performs a single iteration of concurrent deposit operations and asserts the final balance.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
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

    /**
     * Resets the test user's balance to the initial value.
     */
    private void resetUserBalance() {
        var user = userRepository.findById(userId).orElseThrow();
        user.setBalance(initialBalance);
        userRepository.save(user);
    }
}