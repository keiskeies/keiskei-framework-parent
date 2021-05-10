package top.keiskeiframework.dashboard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.keiskeiframework.common.enums.IErrorCode;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/10 17:24
 */
@Getter
@AllArgsConstructor
public enum DashboardExceptionEnum implements IErrorCode {

    //
    TYPE_CONFLICT(50301L, "图表类型冲突"),
    TYPE_EMPTY(50302L, "时间类型必须输入间隔方式"),
    ENTITY_FIELD_NOT_EXIST(50401L, "实体类字段不存在")


    ;

    private final long code;

    private final String msg;
}
