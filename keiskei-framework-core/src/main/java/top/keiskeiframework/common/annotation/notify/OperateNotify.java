package top.keiskeiframework.common.annotation.notify;

import top.keiskeiframework.common.enums.notify.OperateNotifyType;

import java.lang.annotation.*;

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
@Inherited
public @interface OperateNotify {

    /**
     * 操作类型
     * @return .
     */
    OperateNotifyType type();
}
