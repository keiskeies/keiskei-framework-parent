package top.keiskeiframework.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.keiskeiframework.common.enums.exception.ApiErrorCode;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.R;

import java.util.stream.Collectors;

/**
 * <p>
 * 全局异常拦截
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午3:20:17
 */
@ControllerAdvice({"top.keiskeiframework", "org.springframework"})
@Component
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 业务异常处理
     * @param e exception
     * @return json
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R<?> error(BizException e) {
        return R.failed(e.getCode(), e.getMessage());
    }

    /**
     * 异常处理
     * @param e exception
     * @return json
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R<?> error(Exception e) {
        Long code = ApiErrorCode.FAILED.getCode();
        String message;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            message = methodArgumentNotValidException.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",\n"));
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            message = "参数解析异常";
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "请求方式错误";
        } else if (e instanceof DuplicateKeyException) {
            message = "该条数据已存在";
        } else if (e instanceof DataIntegrityViolationException) {
            message = "该条数据被引用";
        } else {
            message = e.getMessage();
        }
        R<?> r = R.failed(code, message);
        log.error("全局异常:", e);
        return r;
    }
}
