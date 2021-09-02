package ${module.packageName}.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * <p>
 * ${table.comment!} ${field.comment!} 字段枚举
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
public enum ${table.name?cap_first}${field.name?cap_first}Enum {

<#list field.fieldEnums as enum>
    // ${enum.comment}
    ${enum.name},
</#list>
    ;


}
