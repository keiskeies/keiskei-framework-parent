package top.keiskeiframework.generate.enums;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 字段类型枚举
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/18 17:32
 */
@Getter
@AllArgsConstructor
public enum FieldInfoTypeEnum {
    //
    NUMBER("Integer"),
    DECIMAL("Double"),
    MONEY("Long"),
    DATE("LocalDate"),
    TIME("LocalTime"),
    DATE_TIME("LocalDateTime"),
    WORD("String"),
    LONG_WORD("String"),
    LONG_TEXT("String"),
    TO_LONG_TEXT("String"),
    MIDDLE_ID(""),
    IMAGE("String"),
    VIDEO("String"),
    FILE("String"),
    VISIT_TIMES("Integer"),
    ENABLE("Boolean"),
    SORT("Long"),
    DICTIONARY(""),
    TAG("String")

    ;

    private final String value;

}
