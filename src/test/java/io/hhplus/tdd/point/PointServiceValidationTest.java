package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointServiceValidationTest {

    PointServiceValidation validation;

    @BeforeEach
    void setUp() {
        validation = new PointServiceValidation();
    }

    @Test
    @DisplayName("ID가 - 일 경우")
    void validUserIdTest01() {
        // given
        long id = -1L;

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> validation.validUserId(id));

        // then
        assertEquals("조회할 ID는 1 이상이어야 합니다.", throwable.getMessage());
    }

    @Test
    @DisplayName("ID가 0일 경우")
    void validUserIdTest02() {
        // given
        long id = 0L;

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> validation.validUserId(id));

        // then
        assertEquals("조회할 ID는 1 이상이어야 합니다.", throwable.getMessage());
    }

    @Test
    @DisplayName("AMOUNT가 - 일 경우")
    void validAmountTest01() {
        // given
        long amount = -1000L;

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> validation.validAmount(amount));

        // then
        assertEquals("입력된 포인트는 1 이상이어야 합니다.", throwable.getMessage());
    }

    @Test
    @DisplayName("AMOUNT가 0일 경우")
    void validAmountTest02() {
        // given
        long amount = 0L;

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> validation.validAmount(amount));

        // then
        assertEquals("입력된 포인트는 1 이상이어야 합니다.", throwable.getMessage());
    }

    @Test
    @DisplayName("충전될 잔고가 1,000,000 초과될 경우")
    void validMaxSavePointTest01() {
        // given
        long point = 1000100L;

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> validation.validMaxSavePoint(point));

        // then
        assertEquals("포인트 최대 잔고는 1,000,000 입니다.", throwable.getMessage());
    }

    @Test
    @DisplayName("사용할 포인트 잔고가 부족할 경우")
    void validUsePointTest01() {
        // given
        long point = 1000L;
        long amount = 2000L;

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> validation.validUsePoint(point, amount));

        // then
        assertEquals("포인트 잔고가 부족합니다.", throwable.getMessage());
    }

}