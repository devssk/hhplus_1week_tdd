package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;
    private final UserPointRepository userPointRepository;
    private final PointServiceValidation validation;

    public UserPoint charge(long id, long amount) {
        validation.validUserId(id);
        validation.validAmount(amount);

        UserPoint findUserPoint = userPointRepository.selectById(id);

        long updatePoint = findUserPoint.point() + amount;

        validation.validMaxSavePoint(updatePoint);

        UserPoint saveUserPoint = userPointRepository.insertOrUpdate(id, updatePoint);

        long updatedMillis = saveUserPoint.updateMillis();

        pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, updatedMillis);

        return saveUserPoint;
    }

    public UserPoint use(long id, long amount) {
        validation.validUserId(id);
        validation.validAmount(amount);

        UserPoint findUserPoint = userPointRepository.selectById(id);

        validation.validUsePoint(findUserPoint.point(), amount);

        long updatePoint = findUserPoint.point() - amount;

        UserPoint updateUserPoint = userPointRepository.insertOrUpdate(id, updatePoint);

        long updatedMillis = updateUserPoint.updateMillis();

        pointHistoryRepository.insert(id, amount, TransactionType.USE, updatedMillis);

        return updateUserPoint;
    }

}
