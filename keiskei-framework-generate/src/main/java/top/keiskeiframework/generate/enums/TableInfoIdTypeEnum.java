package top.keiskeiframework.generate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 表ID类型枚举
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/2 23:43
 */
@Getter
@AllArgsConstructor
public enum TableInfoIdTypeEnum {
    /**
     * String
     */
    STRING("String"),
    /**
     * Long
     */
    LONG("Long"),
    /**
     * Integer
     */
    INTEGER("Integer")

    ;
    private final String value;
}
