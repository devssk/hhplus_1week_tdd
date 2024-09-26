package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;
    private final UserPointRepository userPointRepository;
    private final PointServiceValidation validation;
    private final PointServiceLock pointServiceLock;

    public UserPoint charge(long id, long amount) {
        validation.validUserId(id);
        validation.validAmount(amount);

        Lock lock = pointServiceLock.getLock(id);
        lock.lock();
        try {
            UserPoint findUserPoint = userPointRepository.selectById(id);

            long updatePoint = findUserPoint.point() + amount;

            validation.validMaxSavePoint(updatePoint);

            UserPoint saveUserPoint = userPointRepository.insertOrUpdate(id, updatePoint);

            long updatedMillis = saveUserPoint.updateMillis();

            pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, updatedMillis);

            return saveUserPoint;
        } finally {
            lock.unlock();
        }
    }

    public UserPoint use(long id, long amount) {
        validation.validUserId(id);
        validation.validAmount(amount);

        Lock lock = pointServiceLock.getLock(id);
        lock.lock();
        try {
            UserPoint findUserPoint = userPointRepository.selectById(id);

            validation.validUsePoint(findUserPoint.point(), amount);

            long updatePoint = findUserPoint.point() - amount;

            UserPoint updateUserPoint = userPointRepository.insertOrUpdate(id, updatePoint);

            long updatedMillis = updateUserPoint.updateMillis();

            pointHistoryRepository.insert(id, amount, TransactionType.USE, updatedMillis);

            return updateUserPoint;
        } finally {
            lock.unlock();
        }
    }

    public UserPoint getUserPoint(long id) {
        validation.validUserId(id);

        return userPointRepository.selectById(id);
    }

    public List<PointHistory> getPointHistoryList(long id) {
        validation.validUserId(id);

        return pointHistoryRepository.selectAllByUserId(id);
    }

}
