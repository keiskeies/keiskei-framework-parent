package top.keiskeiframework.generate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/22 19:37
 */
@Getter
@AllArgsConstructor
public enum FieldInfoQueryTypeEnum {

    //
    INPUT("精确查询"),
    FUZZY_INPUT("模糊查询"),
    SELECT("单选框"),
    MULTI_SELECT("多选框"),
    RANGE("范围选择"),


    ;

    private final String msg;
}
