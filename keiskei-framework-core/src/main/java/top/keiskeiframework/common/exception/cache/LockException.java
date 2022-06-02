package top.keiskeiframework.common.exception.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.enums.exception.ApiErrorCode;
import top.keiskeiframework.common.enums.exception.IErrorCode;


/**
 * <p>
 * 锁业务异常
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LockException extends RuntimeException {

    private static final long serialVersionUID = -7098200834996845904L;

    /**
     * 错误码
     */
    private Long code;

    /**
     * 错误信息
     */
    private String message;

    public LockException(IErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }


    public LockException(String message) {
        this.code = ApiErrorCode.FAILED.getCode();
        this.message = message;
    }

    public LockException(long code, String message) {
        this.code = code;
        this.message = message;
    }

}
