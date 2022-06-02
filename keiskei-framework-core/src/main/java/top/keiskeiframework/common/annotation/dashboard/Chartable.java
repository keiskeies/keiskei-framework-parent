package top.keiskeiframework.common.annotation.dashboard;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 允许显示统计图表
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/11/23 14:20
 */

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Chartable {
}
