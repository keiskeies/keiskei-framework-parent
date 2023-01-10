package top.keiskeiframework.common.base.mp.annotation;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.Transient;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * ManyToOne
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 21:56
 */
@Documented
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Transient
@TableField
public @interface MpManyToOne {

    String filedName();

    @AliasFor(annotation = TableField.class)
    boolean exist() default false;
}