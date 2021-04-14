package top.keiskeiframework.common.annotation.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/9/25 5:53 下午
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestModel {

    String value() default "";
}
