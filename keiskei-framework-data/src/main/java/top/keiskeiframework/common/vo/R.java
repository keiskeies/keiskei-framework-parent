package top.keiskeiframework.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.enums.exception.ApiErrorCode;
import top.keiskeiframework.common.enums.exception.IErrorCode;

import java.io.Serializable;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 数据统一返回结构
 * </p>
 * @since 2020/11/22 21:19
 */
@Data
@ApiModel(value = "R", description = "统一相应")
public class R<T> implements Serializable {
    private static final long serialVersionUID = 4464081726731277091L;

    @ApiModelProperty(value = "状态码", dataType = "Long", example = "0")
    private long code;

    @ApiModelProperty(value = "数据", dataType = "Object/Array")
    private T data;

    @ApiModelProperty(value = "错误信息", dataType = "String", example = "操作成功")
    private String msg;

    public static <T> R<T> ok(T data) {
        return restResult(data, ApiErrorCode.SUCCESS);
    }

    public static <T> R<T> ok(T data, String message) {
        return restResult(data, ApiErrorCode.SUCCESS.getCode(), message);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, ApiErrorCode.FAILED.getCode(), msg);
    }

    public static <T> R<T> failed(IErrorCode errorCode) {
        return restResult(null, errorCode);
    }

    public static <T> R<T> restResult(T data, IErrorCode errorCode) {
        return restResult(data, errorCode.getCode(), errorCode.getMsg());
    }

    private static <T> R<T> restResult(T data, long code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

}