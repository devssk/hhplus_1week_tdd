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
class GetUserPointTest {

    @InjectMocks
    PointService pointService;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    UserPointRepository userPointRepository;

    @Mock
    PointServiceValidation pointServiceValidation;

    /*
    * 정상 조회 시 결과가 반환되고 반환된 값들 검증
    * 조회에 대한 테스트는 굳이 안해도 될 것 같다.
    * */
    @Test
    @DisplayName("정상 조회 건")
    void getUserPointTest01() {
        // given
        long id = 1L;
        long point = 1500L;
        doReturn(new UserPoint(id, point, System.currentTimeMillis())).when(userPointRepository).selectById(anyLong());

        // when
        UserPoint result = pointService.getUserPoint(id);

        // then
        assertAll(() -> {
            assertEquals(id, result.id());
            assertEquals(point, result.point());
        });
    }

}