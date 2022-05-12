package top.keiskeiframework.common.base.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.Transient;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 22:01
 */
@Target({FIELD})
@Retention(RUNTIME)
@Transient
public @interface OneToOne {

    String filedName();
}
