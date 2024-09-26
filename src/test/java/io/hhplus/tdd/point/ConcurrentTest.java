package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcurrentTest {

    @Autowired
    PointService pointService;

    @Autowired
    UserPointTable userPointTable;

    @BeforeEach
    void setUp() {
        userPointTable.insertOrUpdate(1L, 0);
        userPointTable.insertOrUpdate(2L, 0);
        userPointTable.insertOrUpdate(3L, 0);
        userPointTable.insertOrUpdate(4L, 0);
    }

    @Test
    @DisplayName("한 유저가 1000포인트 100번 충전 진행 동시성 테스트")
    void chargePointSingleUserConcurrentTest() throws InterruptedException {
        // given
        long id = 1L;
        long amount = 1000L;

        // when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    pointService.charge(id, amount);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        UserPoint userPoint = pointService.getUserPoint(id);

        // then
        assertEquals(amount * threadCount, userPoint.point());
    }

    @Test
    @DisplayName("4명의 유저가 각각 1000포인트, 500포인트, 1500포인트, 5000포인트로 각각 25번 충전 진행 동시성 테스트")
    void chargePointFourUserConcurrentTest() {
        // given
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 3L;
        long id4 = 4L;
        long amount1 = 1000L;
        long amount2 = 500L;
        long amount3 = 1500L;
        long amount4 = 5000L;

        // when
        CompletableFuture[] futures = new CompletableFuture[100];
        for (int i = 0; i < 25; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.charge(id1, amount1));
        }
        for (int i = 25; i < 50; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.charge(id2, amount2));
        }
        for (int i = 50; i < 75; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.charge(id3, amount3));
        }
        for (int i = 75; i < 100; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.charge(id4, amount4));
        }

        CompletableFuture.allOf(futures).join();


        UserPoint userPoint1 = pointService.getUserPoint(id1);
        UserPoint userPoint2 = pointService.getUserPoint(id2);
        UserPoint userPoint3 = pointService.getUserPoint(id3);
        UserPoint userPoint4 = pointService.getUserPoint(id4);

        // then
        assertAll(() -> {
            assertEquals(amount1 * 25, userPoint1.point());
            assertEquals(amount2 * 25, userPoint2.point());
            assertEquals(amount3 * 25, userPoint3.point());
            assertEquals(amount4 * 25, userPoint4.point());
        });

    }

    @Test
    @DisplayName("한 유저가 20000포인트가 충전된 상태에서 100포인트 100번 사용 진행 동시성 테스트")
    void usePointSingleUserConcurrentTest() throws InterruptedException {
        // given
        long id = 1L;
        long point = 20000L;
        long amount = 100L;

        pointService.charge(id, point);

        // when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    pointService.use(id, amount);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        UserPoint userPoint = pointService.getUserPoint(id);

        // then
        assertEquals(point - (amount * threadCount), userPoint.point());
    }

    @Test
    @DisplayName("4명의 유저가 1,000,000 충전된 상태에서 각각 1000포인트, 500포인트, 1500포인트, 5000포인트로 각각 25번 사용 진행 동시성 테스트")
    void usePointFourUserConcurrentTest() {
        // given
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 3L;
        long id4 = 4L;
        long point = 1000000L;
        long amount1 = 1000L;
        long amount2 = 500L;
        long amount3 = 1500L;
        long amount4 = 5000L;

        pointService.charge(id1, point);
        pointService.charge(id2, point);
        pointService.charge(id3, point);
        pointService.charge(id4, point);

        // when
        CompletableFuture[] futures = new CompletableFuture[100];
        for (int i = 0; i < 25; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.use(id1, amount1));
        }
        for (int i = 25; i < 50; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.use(id2, amount2));
        }
        for (int i = 50; i < 75; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.use(id3, amount3));
        }
        for (int i = 75; i < 100; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.use(id4, amount4));
        }
        CompletableFuture.allOf(futures).join();

        UserPoint userPoint1 = pointService.getUserPoint(id1);
        UserPoint userPoint2 = pointService.getUserPoint(id2);
        UserPoint userPoint3 = pointService.getUserPoint(id3);
        UserPoint userPoint4 = pointService.getUserPoint(id4);

        // then
        assertAll(() -> {
            assertEquals(point - (amount1 * 25), userPoint1.point());
            assertEquals(point - (amount2 * 25), userPoint2.point());
            assertEquals(point - (amount3 * 25), userPoint3.point());
            assertEquals(point - (amount4 * 25), userPoint4.point());
        });

    }

    @Test
    @DisplayName("한 유저가 10000포인트 충전된 상태에서 1000포인트 50번 충전 500포인트 50번 사용 진행 동시성 테스트")
    void chargePointAndUsePointSingleUserConcurrentTest() throws InterruptedException {
        // given
        long id = 1L;
        long point = 10000L;
        long saveAmount = 1000L;
        long useAmount = 500L;

        pointService.charge(id, point);

        // when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < 50; i++) {
            executorService.execute(() -> {
                try {
                    pointService.charge(id, saveAmount);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        for (int i = 50; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    pointService.use(id, useAmount);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        UserPoint userPoint = pointService.getUserPoint(id);

        // then
        assertEquals(point + (50 * saveAmount) - (50 * useAmount), userPoint.point());
    }

    @Test
    @DisplayName("2명의 유저가 10000포인트 충전된 상태에서 2500포인트 25번 충전 1500포인트 25번 사용, 5000포인트 25번 충전 1000포인트 25번 사용 동시성 테스트")
    void chargePointAndUsePointTwoUserConcurrentTest() {
        // given
        long id1 = 1L;
        long id2 = 2L;
        long point = 10000L;
        long saveAmount1 = 2500L;
        long useAmount1 = 1500L;
        long saveAmount2 = 5000L;
        long useAmount2 = 1000L;

        pointService.charge(id1, point);
        pointService.charge(id2, point);

        // when
        CompletableFuture[] futures = new CompletableFuture[100];
        for (int i = 0; i < 25; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.charge(id1, saveAmount1));
        }
        for (int i = 25; i < 50; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.use(id1, useAmount1));
        }
        for (int i = 50; i < 75; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.charge(id2, saveAmount2));
        }
        for (int i = 75; i < 100; i++) {
            futures[i] = CompletableFuture.runAsync(() -> pointService.use(id2, useAmount2));
        }
        CompletableFuture.allOf(futures).join();

        UserPoint userPoint = pointService.getUserPoint(id1);
        UserPoint userPoint2 = pointService.getUserPoint(id2);

        // then
        assertEquals(point + (saveAmount1 * 25) - (useAmount1 * 25), userPoint.point());
        assertEquals(point + (saveAmount2 * 25) - (useAmount2 * 25), userPoint2.point());

    }

}