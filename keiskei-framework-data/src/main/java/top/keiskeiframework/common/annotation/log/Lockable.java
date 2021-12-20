package top.keiskeiframework.common.annotation.log;

import org.springframework.lang.Nullable;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redis锁工具
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/12/11 0:29
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Lockable {
    String lockName() default "";
    /**
     * 锁键值
     * 默认key为类名+方法名，可以用spel表达式自定义，加上方法参数
     *
     * @return 。
     */
    String key() default "";

    /**
     * 加锁等待时间
     *
     * @return 。
     */
    int waitTime() default 3000;

    /**
     * 等待/超时时间 时间单位
     *
     * @return .
     */
    TimeUnit lockTimeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 错误提示信息
     *
     * @return 。
     */
    String message() default "点击过于频繁，请稍后再试";

}
