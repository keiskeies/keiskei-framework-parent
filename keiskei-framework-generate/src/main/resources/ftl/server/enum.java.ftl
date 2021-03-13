package ${module.packageName}.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * <p>
 * ${enum.comment!} 字段枚举
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
@Getter
@AllArgsConstructor
public enum ${enum.name} {

    //
<#list enum.fields as field>
    ${enum.name?upper_case}_${field.code}(${field.code}, ${field.msg}),
</#list>
    ;

    private final int code;
    private final String msg;

}
