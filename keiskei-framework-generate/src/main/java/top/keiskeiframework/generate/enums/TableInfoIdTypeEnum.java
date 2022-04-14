package top.keiskeiframework.generate.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

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
public enum TableInfoIdTypeEnum implements IEnum<Integer> {
    /**
     * String
     */
    STRING(0, "String"),
    /**
     * Long
     */
    LONG(1,"Long"),
    /**
     * Integer
     */
    INTEGER(2,"Integer")

    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String name;
    @JsonCreator
    public static TableInfoIdTypeEnum getByValue(int value) {
        for (TableInfoIdTypeEnum tableInfoIdTypeEnum : TableInfoIdTypeEnum.values()) {
            if (Objects.equals(value, tableInfoIdTypeEnum.getValue())) {
                return tableInfoIdTypeEnum;
            }
        }
        return null;
    }
}
