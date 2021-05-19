package ${module.packageName}.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * <p>
 * ${table.comment!} ${filed.comment!} 字段枚举
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
public enum ${table.name?uncap_first}${field.name?uncap_first}Enum {

<#list field.fieldEnums as enum>
    // ${enum.comment}
    ${enum.name},
</#list>
    ;


}
