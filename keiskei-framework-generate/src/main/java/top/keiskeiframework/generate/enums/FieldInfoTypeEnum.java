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
    MONEY("Integer"),
    DATE("LocalDate"),
    TIME("LocalTime"),
    DATE_TIME("LocalDateTime"),
    WORD("String"),
    Integer_WORD("String"),
    Integer_TEXT("String"),
    HTML("String"),
    MIDDLE_ID(""),
    IMAGE("String"),
    VIDEO("String"),
    FILE("String"),
    VISIT_TIMES("Integer"),
    ENABLE("Boolean"),
    SORT("ID"),
    DICTIONARY(""),
    TAGS("String")

    ;

    private final String value;

}
