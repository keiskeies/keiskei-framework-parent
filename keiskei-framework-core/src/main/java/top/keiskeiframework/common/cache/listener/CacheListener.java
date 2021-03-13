package top.keiskeiframework.common.cache.listener;


/**
 * <p>
 * 缓存监听
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/11/3 2:17
 */
public interface CacheListener {
    /**
     * 键失效回调
     * @param key 缓存键
     */
    void failureKey(Object key);
}
