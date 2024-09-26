package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChargeTest {

    @InjectMocks
    PointService pointService;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    UserPointRepository userPointRepository;

    @Mock
    PointServiceValidation pointServiceValidation;

    @Mock
    PointServiceLock pointServiceLock;

    @Test
    @DisplayName("정상 충전")
    void chargeTest01() {
        // given
        long id = 1L;
        long amount = 1000L;

        doReturn(new UserPoint(id, 0, System.currentTimeMillis())).when(userPointRepository).selectById(anyLong());
        doReturn(new UserPoint(id, amount, System.currentTimeMillis())).when(userPointRepository).insertOrUpdate(anyLong(), anyLong());
        doReturn(new ReentrantLock()).when(pointServiceLock).getLock(anyLong());

        // when
        UserPoint result = pointService.charge(id, amount);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(amount, result.point());
        });
    }

}