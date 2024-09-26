package io.hhplus.tdd.point;

public interface UserPointRepository {
    UserPoint insertOrUpdate(long id, long amount);
    UserPoint selectById(Long id);
}
