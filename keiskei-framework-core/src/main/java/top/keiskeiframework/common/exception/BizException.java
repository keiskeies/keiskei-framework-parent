package top.keiskeiframework.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.enums.exception.ApiErrorCode;
import top.keiskeiframework.common.enums.exception.IErrorCode;


/**
 * <p>
 * 全局业务异常
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -7098200834996845904L;

    /**
     * 错误码
     */
    private Long code;

    /**
     * 错误信息
     */
    private String message;

    public BizException(IErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }


    public BizException(String message) {
        this.code = ApiErrorCode.FAILED.getCode();
        this.message = message;
    }

    public BizException(long code, String message) {
        this.code = code;
        this.message = message;
    }

}
