package top.keiskeiframework.cpreading.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * <p>
 * 读者管理 性别 字段枚举
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public enum ReaderSexEnum {
    // 未知
    NONE,

    // 男
    MALE,

    // 女
    FEMALE,
    ;

    public static ReaderSexEnum convert(int index) {
        return ReaderSexEnum.values()[index];
    }


}
