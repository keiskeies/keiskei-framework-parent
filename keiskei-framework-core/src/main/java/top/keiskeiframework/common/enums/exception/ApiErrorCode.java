package top.keiskeiframework.common.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/22 21:22
 */
@Getter
@AllArgsConstructor
public enum ApiErrorCode implements IErrorCode{

    //
    FAILED(-1L, "操作失败"),
    SUCCESS(0L, "执行成功");

    private final long code;
    private final String msg;

    public static ApiErrorCode fromCode(long code) {
        for (ApiErrorCode apiErrorCode : ApiErrorCode.values()) {
            if (apiErrorCode.getCode() == code) {
                return apiErrorCode;
            }
        }
        return SUCCESS;
    }
}
