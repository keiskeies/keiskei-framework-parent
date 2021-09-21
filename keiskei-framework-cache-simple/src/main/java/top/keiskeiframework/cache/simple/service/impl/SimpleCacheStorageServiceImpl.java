package top.keiskeiframework.cache.simple.service.impl;

import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.keiskeiframework.cache.config.LimitRequestProperties;
import top.keiskeiframework.cache.service.CacheStorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * <p>
 *
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/12/9 17:43
 */
@Service
@Primary
public class SimpleCacheStorageServiceImpl implements CacheStorageService {

    @Autowired
    private LimitRequestProperties limitRequestProperties;


    private static Set<RequestLimitRule> rules;
    private static RequestRateLimiter limiter;

    private static final ConcurrentHashMap<String, Object> CACHE = new ConcurrentHashMap<>();


    @Override
    public Map<String, Object> list() {
        return CACHE;
    }

    @Override
    public void save(String key, Object value) {
        CACHE.put(key, value);
    }

    @Override
    public void update(String key, Object value) {
        save(key, value);
    }

    @Override
    public void save(String key, Object value, long timeout, TimeUnit unit) {
        save(key, value);
        timeOutDispose(key, timeout, unit);

    }

    @Override
    public void update(String key, Object value, long timeout, TimeUnit unit) {
        save(key, value, timeout, unit);
        timeOutDispose(key, timeout, unit);
    }

    @Override
    public Object get(String key) {

        return CACHE.getOrDefault(key, null);
    }

    @Override
    public List<Object> getLikeKey(String key) {
        Pattern pattern = Pattern.compile(key);
        List<Object> result = new ArrayList<>();
        CACHE.forEach((s, o) -> {
            if (pattern.matcher(s).find()) {
                result.add(o);
            }
        });
        return result;
    }

    @Override
    public Boolean exist(String key) {
        return CACHE.contains(key);
    }

    @Override
    public void del(String key) {
        CACHE.remove(key);
    }

    @Override
    public void del(Collection<String> keys) {
        for (String key : keys) {
            del(key);
        }
    }

    @Override
    public void del(String... keys) {
        for (String key : keys) {
            del(key);
        }
    }

    @Override
    public void delAll() {
        CACHE.clear();
    }

    @Async
    public void timeOutDispose(String key, long timeOut, TimeUnit unit) {
        try {
            unit.sleep(timeOut);
            CACHE.remove(key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean overTimeNum(String key) {

        if (null == rules) {
            rules = Collections.singleton(RequestLimitRule.of(limitRequestProperties.getMinutes(), TimeUnit.MINUTES, limitRequestProperties.getTimes()));
            limiter = new InMemorySlidingWindowRequestRateLimiter(rules);
        }
        return limiter.overLimitWhenIncremented(key);
    }
}
