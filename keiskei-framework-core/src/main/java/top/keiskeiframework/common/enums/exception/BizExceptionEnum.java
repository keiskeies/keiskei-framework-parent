package top.keiskeiframework.common.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 基础业务异常枚举
 * </p>
 *
 * @author 陈加敏
 * @since 2019/7/19 14:32
 */
@Getter
@AllArgsConstructor
public enum BizExceptionEnum implements IErrorCode {

    //
    AUTH_ERROR(400201L, "会话已超时！"),
    AUTH_FORBIDDEN(400202L, "权限认证失败！"),
    AUTH_USER_NOT_FOND(400203L, "用户名不存在！"),
    AUTH_PASSWORD_ERROR(400204L, "密码错误！"),

    AUTH_ACCOUNT_LOCKED(400205L, "该账号已经被锁定！请10分钟后再试"),
    AUTH_ACCOUNT_EXPIRED(400206L, "该账号已过期！"),
    AUTH_PASSWORD_EXPIRED(400207L, "长时间未修改密码"),


    CHECK_FIELD(500101, "必填字段校验错误!"),
    RESOLVE_FIELD_ERROR(500102, "参数解析错误!"),

    DELETE_ERROR(500201, "删除失败"),
    NOT_FOUND_ERROR(500202, "数据不存在"),
    EXPORT_ERROR(500203, "导出失败"),
    SAVE_ERROR(500204, "保存失败！"),

    //支付error
    WXPREPAYERROR(401L, "微信支付错误"),
    ALIPAYERROR(402L, "支付宝支付错误"),

    ERROR(500L, "网络开小差了, 请稍后重试"),

    ;

    private final long code;

    private final String msg;
}
