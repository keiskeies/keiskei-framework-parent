package top.keiskeiframework.cache.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.cache.config.LimitRequestProperties;
import top.keiskeiframework.cache.service.CacheStorageService;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis数据操作dao层实现层
 *
 * @author 陈加敏
 * @since 2019/7/15 14:07
 */
@Service
public class RedisCacheStorageServiceImpl implements CacheStorageService {

    @Autowired
    private LimitRequestProperties limitRequestProperties;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TEMP_FORMAT = "%s_temp";

    @Override
    public Map<String, Object> list() {
        Set<String> keys = redisTemplate.keys("*");
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        List<String> keyList = keys.stream().sorted().collect(Collectors.toList());
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : keyList) {
            result.put(key, redisTemplate.opsForValue().get(key));
        }
        return result;
    }

    @Override
    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void update(String key, Object value) {
        String tempKey = String.format(TEMP_FORMAT, key);
        save(tempKey, value);
        redisTemplate.rename(tempKey, key);
    }

    @Override
    public void save(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void update(String key, Object value, long timeout, TimeUnit unit) {
        String tempKey = String.format(TEMP_FORMAT, key);
        save(tempKey, value, timeout, unit);
        redisTemplate.rename(tempKey, key);
    }

    @Override
    public Object get(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("正确的键值不能为空");
        }
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public List<Object> getLikeKey(String key) {
        Set<String> keys = redisTemplate.keys(key);
        List<Object> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(keys)) {
            return result;
        }
        for (String keyTemp : keys) {
            result.add(get(keyTemp));
        }
        return result;
    }

    @Override
    public Boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void del(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("正确的键值不能为空");
        }
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    @Override
    public void del(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            throw new RuntimeException("正确的键值不能为空");
        }
        for (String k : keys) {
            if (StringUtils.isEmpty(k)) {
                throw new RuntimeException("正确的键值不能为空");
            }
        }
        redisTemplate.opsForValue().getOperations().delete(keys);
    }

    @Override
    public void del(String... keys) {
        Set<String> keySet = new HashSet<>(Arrays.asList(keys));
        this.del(keySet);

    }

    @Override
    public Boolean overTimeNum(String key) {

        redisTemplate.opsForValue().setIfAbsent(key, 0, limitRequestProperties.getMinutes(), TimeUnit.MINUTES);
        Long times = redisTemplate.opsForValue().increment(key, 1L);
        if (times == null) {
            redisTemplate.opsForValue().setIfAbsent(key, 1, limitRequestProperties.getMinutes(), TimeUnit.MINUTES);
            times = 1L;
        }
        return times >= limitRequestProperties.getTimes();

    }

    @Override
    public void delAll() {
        Set<String> keys = redisTemplate.keys("*");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
