package top.keiskeiframework.file.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 文件分片上传base64
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/9/29 13:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface UploadBlobPart {
    String[] value();
}
