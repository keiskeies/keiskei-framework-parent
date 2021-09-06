package top.keiskeiframework.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/6 22:40
 */
@Service
public class TestService {

    @Cacheable(cacheNames="TEST", key = "#id", unless = "#result == null")
    public String getById(Long id) {
        return UUID.randomUUID().toString();
    }
}
