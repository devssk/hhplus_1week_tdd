package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UseTest {

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

    /*
    * 정상 사용 시 결과 값이 반환 되는지 확인
    * */
    @Test
    @DisplayName("정상 사용 건")
    void useTest01() {
        // given
        long id = 1L;
        long point = 3000L;
        long amount = 2000L;

        doReturn(new UserPoint(id, point, System.currentTimeMillis())).when(userPointRepository).selectById(anyLong());
        doReturn(new UserPoint(id, point - amount, System.currentTimeMillis())).when(userPointRepository).insertOrUpdate(anyLong(), anyLong());
        doReturn(new ReentrantLock()).when(pointServiceLock).getLock(anyLong());

        // when
        UserPoint result = pointService.use(id, point);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(point - amount, result.point());
        });

    }

}