package top.keiskeiframework.common.annotation.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * findAllByColumn缓存字段
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2012/04/16 22.36
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchCacheField {
}
