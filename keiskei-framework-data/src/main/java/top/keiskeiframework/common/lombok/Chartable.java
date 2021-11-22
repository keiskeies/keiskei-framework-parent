package top.keiskeiframework.common.lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 表格统计实体类加上 index， indexNumber为参数的构造方法
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/16 00:26
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Chartable {

}
