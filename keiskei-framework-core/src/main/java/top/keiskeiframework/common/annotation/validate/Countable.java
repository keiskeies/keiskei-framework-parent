package top.keiskeiframework.common.annotation.validate;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 访问叠加
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

    String key() default "";

    String lockName() default "SUPERPOSITION";


}
