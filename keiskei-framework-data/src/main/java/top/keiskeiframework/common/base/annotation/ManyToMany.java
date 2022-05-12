package top.keiskeiframework.common.base.annotation;

import org.springframework.data.annotation.Transient;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.entity.MiddleEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 21:48
 */
@Target({FIELD})
@Retention(RUNTIME)
@Transient
public @interface ManyToMany {

    Class<? extends MiddleEntity<?, ?>> middleClass();
    Class<? extends BaseEntity<?>> targetClass();

    boolean id1() default true;
}
