package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
* 통합 테스트를 진행하여 실제 포인트가 충전, 사용되는것에 대한 검증
* */
@SpringBootTest
class PointServiceIntegrationTest {

    @Autowired
    PointService pointService;

    @Test
    @DisplayName("충전 테스트 -> 첫 충전, 1500원 충전")
    void chargeTest01() {
        // given
        long id = 1L;
        long amount = 1500L;

        // when
        UserPoint result = pointService.charge(id, amount);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(amount, result.point());
        });
    }

    @Test
    @DisplayName("충전 테스트 -> 여러번 충전, 1500원 충전, 2000원 충전, 700원 충전")
    void chargeTest02() {
        // given
        long id = 2L;
        long amount1 = 1500L;
        long amount2 = 2000L;
        long amount3 = 700L;

        // when
        pointService.charge(id, amount1);
        pointService.charge(id, amount2);
        UserPoint result = pointService.charge(id, amount3);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(amount1 + amount2 + amount3, result.point());
        });
    }

    @Test
    @DisplayName("사용 테스트 -> 3000원 충전, 2000원 사용")
    void useTest01() {
        // given
        long id = 3L;
        long saveAmount = 3000L;
        long useAmount = 2000L;

        // when
        pointService.charge(id, saveAmount);
        UserPoint result = pointService.use(id, useAmount);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(saveAmount - useAmount, result.point());
        });
    }

    @Test
    @DisplayName("사용 테스트 -> 3000원 충전, 2000원 사용, 300원 사용, 700원 사용")
    void useTest02() {
        // given
        long id = 4L;
        long saveAmount = 3000L;
        long useAmount1 = 2000L;
        long useAmount2 = 300L;
        long useAmount3 = 700L;

        // when
        pointService.charge(id, saveAmount);
        pointService.use(id, useAmount1);
        pointService.use(id, useAmount2);
        UserPoint result = pointService.use(id, useAmount3);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(saveAmount - useAmount1 - useAmount2 - useAmount3, result.point());
        });
    }


}