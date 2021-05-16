package top.keiskeiframework.common.enums.exception;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 错误enum主要参数
 * </p>
 * @since 2020/11/22 21:22
 */
public interface IErrorCode {

    /**
     * code
     *
     * @return .
     */
    long getCode();

    /**
     * msg
     *
     * @return .
     */
    String getMsg();
}
