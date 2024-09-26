package io.hhplus.tdd.point;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class PointServiceLock {

    private final ConcurrentHashMap<Long, Lock> locks = new ConcurrentHashMap<>();

    public Lock getLock(long id) {
        return locks.computeIfAbsent(id, lock -> new ReentrantLock(true));
    }

}
