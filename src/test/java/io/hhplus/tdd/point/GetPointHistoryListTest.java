package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GetPointHistoryListTest {

    @InjectMocks
    PointService pointService;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    UserPointRepository userPointRepository;

    @Mock
    PointServiceValidation pointServiceValidation;

    @Test
    @DisplayName("정상 조회 건")
    void getPointHistoryListTest01() {
        // given
        long id = 1L;
        long userId = 1L;
        long point = 1500L;

        List<PointHistory> pointHistoryList = new ArrayList<>();
        pointHistoryList.add(new PointHistory(id, userId, point, TransactionType.CHARGE, System.currentTimeMillis()));

        doReturn(pointHistoryList).when(pointHistoryRepository).selectAllByUserId(anyLong());

        // when
        List<PointHistory> result = pointService.getPointHistoryList(id);

        // then
        assertAll(() -> {
            assertEquals(1, result.size());
        });
    }

}