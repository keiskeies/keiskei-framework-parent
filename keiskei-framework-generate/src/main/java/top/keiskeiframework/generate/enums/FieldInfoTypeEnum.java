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
public enum FieldInfoTypeEnum {
    //
    NUMBER,
    DECIMAL,
    MONEY,
    DATE,
    TIME,
    DATE_TIME,
    WORD,
    LONG_WORD,
    LONG_TEXT,
    TO_LONG_TEXT,
    MIDDLE_ID,
    IMAGE,
    VIDEO,
    FILE,
    VISIT_TIMES,
    ENABLE,
    SORT,
    DICTIONARY
}
