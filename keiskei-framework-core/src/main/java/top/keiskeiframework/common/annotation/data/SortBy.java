package top.keiskeiframework.common.annotation.data;

import java.lang.annotation.*;

/**
 * @author James Chen right_way@foxmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SortBy {

    boolean desc() default true;
}