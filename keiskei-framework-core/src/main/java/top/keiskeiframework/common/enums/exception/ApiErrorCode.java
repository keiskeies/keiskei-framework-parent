package top.keiskeiframework.common.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 基础错误信息
 * </p>
 * @since 2020/11/22 21:22
 */
@Getter
@AllArgsConstructor
public enum ApiErrorCode implements IErrorCode{

    //成功
    FAILED(-1L, "操作失败"),
    // 失败
    SUCCESS(0L, "执行成功");

    /**
     * 状态码
     */
    private final long code;
    /**
     * 错误信息
     */
    private final String msg;

    /**
     * 通过code 获取enum
     * @param code code
     * @return .
     */
    public static ApiErrorCode fromCode(long code) {
        for (ApiErrorCode apiErrorCode : ApiErrorCode.values()) {
            if (apiErrorCode.getCode() == code) {
                return apiErrorCode;
            }
        }
        return SUCCESS;
    }
}
