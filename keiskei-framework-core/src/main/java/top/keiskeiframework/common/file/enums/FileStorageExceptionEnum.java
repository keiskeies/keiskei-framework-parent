package top.keiskeiframework.common.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.keiskeiframework.common.enums.IErrorCode;

/**
 * <p>
 * 文件操作异常枚举
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/11/1 1:48
 */
@Getter
@AllArgsConstructor
public enum FileStorageExceptionEnum implements IErrorCode {
    //
    DIR_NOT_FOUND(701L, "文件夹不存在"),
    DIR_CREATE_ERROR(702L, "文件夹创建失败"),

    FILE_ARGS_ILL(710L, "文件上传参数不合法"),
    FILE_UPLOAD_FAIL(711L, "文件上传失败"),
    FILE_DOWN_FAIL(711L, "文件下载失败"),
    FILE_NOT_FOUND(713L, "文件不存在");
    private final long code;
    private final String msg;
}
