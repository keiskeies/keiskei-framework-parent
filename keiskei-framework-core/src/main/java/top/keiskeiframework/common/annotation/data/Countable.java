package top.keiskeiframework.common.annotation.data;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 访问计数标志
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/12/11 0:29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Countable {

    /**
     * 缓存键
     * @return 。
     */
    String key() default "";

    /**
     * 锁名称
     * @return 。
     */
    String lockName() default "SUPERPOSITION";


}
