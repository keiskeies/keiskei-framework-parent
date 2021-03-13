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
    NUMBER("整数", "Long", null, null, null),
    DECIMAL("小数", "Long", null, null, null),
    MONEY("金额", "Long", null, null, null),
    DATE("日期", "LocalDate", "INPUT_DATE", null, null),
    TIME("时间", "LocalTime", "INPUT_TIME", null, null),
    DATE_TIME("日期时间", "LocalDateTime", "INPUT_DATE_TIME", null, null),
    WORD("文字", "String", "INPUT", null, null),
    LONG_WORD("段落", "String", null, "text", null),
    LONG_TEXT("文章", "String", null, "mediumtext", null),
    TO_LONG_TEXT("大型文章", "String", null, "longtext", null),
    MIDDLE_ID("关联ID", "Long", "SELECT_OPTIONS", null, null),
    IMAGE("图片", "String", null, "text", null),
    VIDEO("视频", "String", null, "text", null),
    FILE("文件", "String", null, "text", null),
    VISIT_TIMES("访问次数", "Long", null, null, null),
    ENABLE("启/禁用", "Boolean", "SELECT_BOOLEAN", null, null),
    SORT("排序", "Long", null, null, null),
    DICTIONARY("枚举", "Integer", "SELECT_ENUMS", null, null);
    private final String propertyName;
    private final String propertyType;
    private final String queryType;
    private final String columnType;
    private final Integer columnLength;

    public static void main(String[] args) {
        JSON.toJSONString(FieldInfoTypeEnum.values());
    }
}
