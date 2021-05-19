package top.keiskeiframework.common.annotation.notify;

import top.keiskeiframework.common.enums.OperateNotifyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 操作通知
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/18 17:08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateNotify {

    /**
     * 操作类型
     * @return .
     */
    OperateNotifyType type();
}
