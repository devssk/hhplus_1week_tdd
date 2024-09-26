package io.hhplus.tdd.point;

import org.springframework.stereotype.Component;

@Component
public class PointServiceValidation {

    public void validUserId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("조회할 ID는 1 이상이어야 합니다.");
        }
    }

    public void validAmount(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("입력된 포인트는 1 이상이어야 합니다.");
        }
    }

    public void validMaxSavePoint(long point) {
        if (point > 1000000) {
            throw new IllegalArgumentException("포인트 최대 잔고는 1,000,000 입니다.");
        }
    }

    public void validUsePoint(long point, long amount) {
        if (point < amount) {
            throw new IllegalArgumentException("포인트 잔고가 부족합니다.");
        }
    }

}
