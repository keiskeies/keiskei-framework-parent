package top.keiskeiframework.common.aop;

import org.springframework.dao.DataIntegrityViolationException;
import top.keiskeiframework.common.enums.ApiErrorCode;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局的的异常拦截器（拦截所有的控制器）
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午3:20:17
 */
@ControllerAdvice()
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 拦截全局异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R<?> error(Exception e) {
        long code = ApiErrorCode.FAILED.getCode();
        String message;
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            code = bizException.getCode();
            message = bizException.getMessage();
        } else if (e instanceof MethodArgumentNotValidException) {
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
            log.error("全局异常:", e);
            message = e.getMessage();
        }
        R<?> r = R.failed(message);
        r.setCode(code);

        return r;
    }
}