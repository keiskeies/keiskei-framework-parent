package top.keiskeiframework.common.base.annotation;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.core.annotation.AliasFor;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.entity.MiddleEntity;

import java.lang.annotation.Documented;
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
@Documented
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@TableField
public @interface ManyToMany {

    Class<? extends MiddleEntity<?, ?>> middleClass();


    Class<? extends BaseEntity<?>> targetClass();

    boolean id1() default true;

    @AliasFor(annotation = TableField.class)
    boolean exist() default false;

}
