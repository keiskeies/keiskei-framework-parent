package top.keiskeiframework.cache.simple.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/6 23:36
 */
@Service
public class SimpleLockerService {



    public Lock lock(String key) {
        if (REENTRANT_LOCK_MAP.containsKey(key)) {
            return null;
        } else {
            Lock lock = new ReentrantLock(true);
            REENTRANT_LOCK_MAP.put(key, lock);
            return lock;
        }
    }

    public void unlock(String key) {
        Lock lock = REENTRANT_LOCK_MAP.get(key);
        if (null != lock) {
            lock.unlock();
            REENTRANT_LOCK_MAP.remove(key);
        }
    }


    private static final Map<String, Lock> REENTRANT_LOCK_MAP = new ConcurrentHashMap<>();

}
