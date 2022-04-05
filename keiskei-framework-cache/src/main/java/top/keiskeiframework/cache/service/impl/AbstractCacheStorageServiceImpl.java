package top.keiskeiframework.cache.service.impl;

import org.springframework.stereotype.Component;
import top.keiskeiframework.cache.service.CacheStorageService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 缓存实现抽象类
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 16:43
 */
@Component
public class AbstractCacheStorageServiceImpl implements CacheStorageService {

    private static final Map<String, Object> CACHE_MAP = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> list() {
        return null;
    }

    @Override
    public void save(String key, Object value) {
        CACHE_MAP.put(key, value);
    }

    @Override
    public void update(String key, Object value) {
        CACHE_MAP.put(key, value);
    }

    @Override
    public void save(String key, Object value, long timeout, TimeUnit unit) {
        CACHE_MAP.put(key, value);
    }

    @Override
    public void update(String key, Object value, long timeout, TimeUnit unit) {
        CACHE_MAP.put(key, value);
    }

    @Override
    public Object get(String key) {
        return CACHE_MAP.get(key);
    }

    @Override
    public List<Object> getLikeKey(String key) {
        return null;
    }

    @Override
    public Boolean exist(String key) {
        return CACHE_MAP.containsKey(key);
    }

    @Override
    public void del(String key) {
        CACHE_MAP.remove(key);
    }

    @Override
    public void del(Collection<String> keys) {

    }

    @Override
    public void del(String... keys) {

    }

    @Override
    public void delAll() {

    }

    @Override
    public Boolean overTimeNum(String key) {
        return null;
    }
}
