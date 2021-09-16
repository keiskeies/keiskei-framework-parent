package top.keiskeiframework.common.cache.serivce;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 缓存处理接口类
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/11/3 20:47
 */
public interface CacheStorageService {

    /**
     * 获取所有缓存
     * @return .
     */
    Map<String, Object> list();

    /**
     * 保存
     * @param key .
     * @param value .
     */
    void save(String key, Object value);

    /**
     * 更新
     * @param key .
     * @param value .
     */
    void update(String key, Object value);

    /**
     * 限时保存
     * @param key .
     * @param value .
     * @param timeout .
     * @param unit .
     */
    void save(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 限时更新
     * @param key .
     * @param value .
     * @param timeout .
     * @param unit .
     */
    void update(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取
     * @param key .
     * @return .
     */
    Object get(String key);


    /**
     * 模糊查询数据
     * @param key key
     * @return .
     */
    List<Object> getLikeKey(String key);

    /**
     * 判断是否存在
     * @param key .
     * @return .
     */
    Boolean exist(String key);

    /**
     * 删除
     * @param key .
     */
    void del(String key);

    /**
     * 删除多个
     * @param keys .
     */
    void del(Collection<String> keys);

    /**
     * 删除多个
     * @param keys .
     */
    void del(String... keys);

    /**
     * 删除所有
     */
    void delAll();

    /**
     * 定时计数器
     * @param key key
     * @return .
     */
    Boolean overTimeNum(String key);
}
