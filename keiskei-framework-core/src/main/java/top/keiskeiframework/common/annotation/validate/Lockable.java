package top.keiskeiframework.common.annotation.validate;

import org.springframework.lang.Nullable;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/12/11 0:29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Lockable {

    String key() default "";

    String lockName() default "LOCK";

    int waitTime() default 3000;

    int lockTime() default 6000;

    /**
     * 是否使用等待/超时时间
     */
    boolean timing() default false;

    /**
     * 是否使用公平锁
     */
    boolean fail() default false;

    /**
     * 等待/超时时间 时间单位
     */
    TimeUnit lockTimeUnit() default TimeUnit.MILLISECONDS;

    String message() default "点击过于频繁，请稍后再试";

}
