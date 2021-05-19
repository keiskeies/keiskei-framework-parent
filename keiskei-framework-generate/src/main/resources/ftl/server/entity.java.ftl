package ${module.packageName}.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
<#assign parentNam = table.type?lower_case?cap_first>
import top.keiskeiframework.common.base.entity.${parentNam}Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ${module.packageName}.enums.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;
import top.keiskeiframework.common.annotation.validate.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.keiskeiframework.common.util.json.MoneyDeserialize;
import top.keiskeiframework.common.util.json.MoneySerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 * ${table.comment!} 实体类
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "${table.table}")
@ApiModel(value="${table.name}", description="${table.comment!}")
public class ${table.name} extends ${parentName}Entity {

    private static final long serialVersionUID = ${table.serialVersionUID}L;

<#list entity.fields as field>
<#--        字段注释-->
    @ApiModelProperty(value = "${field.comment?trim?replace("\"","'")}", dataType="${field.propertyType}")
<#--        必填校验-->
    <#if field.createRequire || field.updateRequire>
        <#if field.propertyType == "String">@NotBlank<#else>@NotNull</#if>(message = "${field.comment?trim?replace("\"","'")}不能为空", groups = {<#if field.createRequire && field.updateRequire>Insert.class, Update.class<#elseif field.createRequire>Insert.class<#else>Update.class</#if>})
    </#if>
<#--        JsonIgnore-->
    <#if field.jsonIgnore>
    @JsonIgnore
    </#if>
<#--        时间字段序列化-->
    <#if field.propertyType == "LocalDateTime" || field.propertyType == "LocalDate" || field.propertyType == "DateTime">
    @JsonDeserialize(using = ${field.propertyType}Deserializer.class)
    @JsonSerialize(using = ${field.propertyType}Serializer.class)
    @JsonFormat(pattern = "<#if field.propertyType == "LocalDateTime">yyyy-MM-dd HH:mm:ss<#elseif field.propertyType == "LocalDate">yyyy-MM-dd<#else>HH:mm:ss</#if>")
    </#if>
    <#if field.relation??>
        <#--        一对一-->
        <#if field.relation == 'ONE_TO_ONE'>
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "${field.oneToOne?uncap_first}_id")
    private ${field.oneToOne} ${field.oneToOne?uncap_first};

        <#--        一对多-->
        <#elseif field.relation == 'ONE_TO_MANY'>
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="${table.name?lower_case}_id")
    private List<${field.manyTomany}> ${field.manyTomany?uncap_first}s;

        <#--        多对多-->
        <#elseif field.relation == 'MANY_TO_MANY'>
    @ManyToMany(targetEntity = ${field.manyTomany}.class, cascade = CascadeType.DETACH)
    @JoinTable(name = "${field.module?lower_case}_${table.name?lower_case}_${field.manyTomany?lower_case}",
        joinColumns = {@JoinColumn(name = "${table.name?lower_case}_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "${field.manyTomany?lower_case}_id", referencedColumnName = "id")})
    private List<${field.manyTomany}> ${field.manyTomany?uncap_first}s;

        <#--        多对一-->
        <#elseif field.relation == 'MANY_TO_ONE'>
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private ${field.oneToOne} ${field.oneToOne?uncap_first};

        </#if>
    <#else>
<#--        字段类型判断-->
<#--        金钱-->
        <#if field.type == 'MONEY'>
    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
        </#if>
<#--        普通字段-->
    private ${field.propertyType} ${field.propertyName};

    </#if>
</#list>
}
