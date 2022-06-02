package top.keiskeiframework.common.base.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 查询按照字段排序
 * </p>
 * @author James Chen right_way@foxmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SortBy {

    boolean desc() default true;
}
