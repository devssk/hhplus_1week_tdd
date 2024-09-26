package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    @DisplayName("정상 사용 건")
    void useTest01() {
        // given
        long id = 1L;
        long point = 3000L;
        long amount = 2000L;

        doReturn(new UserPoint(id, point, System.currentTimeMillis())).when(userPointRepository).selectById(anyLong());
        doReturn(new UserPoint(id, point - amount, System.currentTimeMillis())).when(userPointRepository).insertOrUpdate(anyLong(), anyLong());

        // when
        UserPoint result = pointService.use(id, point);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(point - amount, result.point());
        });

    }

}