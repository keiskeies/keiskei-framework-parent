package top.keiskeiframework.common.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公共异常枚举
 *
 * @author 陈加敏
 * @since 2019/7/19 14:34
 */
@Getter
@AllArgsConstructor
public enum GlobalExceptionEnum implements IErrorCode {
    //


    SERVER_ERROR(500L, "服务器异常"),
    WRITE_ERROR(502L, "渲染界面错误"),
    FILE_READING_ERROR(503L, "文件读取失败!"),
    FILE_NOT_FOUND(504L, "指定文件未找到!");

    private final long code;
    private final String msg;
}
