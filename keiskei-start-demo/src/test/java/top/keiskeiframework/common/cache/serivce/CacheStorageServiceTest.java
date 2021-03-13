package top.keiskeiframework.common.cache.serivce;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.ApplicationTest;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CacheStorageServiceTest extends ApplicationTest {
    @Autowired
    private CacheStorageService cacheStorageService;

    @Test
    void list() throws InterruptedException {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9999999; i++) {
            sb.append((char) (random.nextInt(2) % 2 == 0 ? 65 : 97 + random.nextInt(26)));
        }

        int threadTotal = 100;
        int clientTotal = 500;
        ExecutorService executorService = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);

        String value = sb.toString();
        for (int i = 0; i< 8;i++) {
            executorService.execute(() -> {
                for (int a = 0; a < Integer.MAX_VALUE; a++) {
                    String key = UUID.randomUUID().toString();
                    log.info("{}", key);
                    cacheStorageService.save(key, value, 60, TimeUnit.SECONDS);
                    cacheStorageService.get(key);
                }
                countDownLatch.countDown();
            });
            countDownLatch.await();
            executorService.shutdown();
        }

    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void testSave() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void get() {
    }

    @Test
    void getLikeKey() {
    }

    @Test
    void exist() {
    }

    @Test
    void del() {
    }

    @Test
    void testDel() {
    }

    @Test
    void testDel1() {
    }

    @Test
    void delAll() {
    }

    @Test
    void overTimeNum() {
    }
}