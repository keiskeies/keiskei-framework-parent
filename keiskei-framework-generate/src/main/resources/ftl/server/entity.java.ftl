package ${module.packageName}.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
<#assign parentName = table.type?lower_case?cap_first>
import top.keiskeiframework.common.base.entity.*;
import top.keiskeiframework.common.util.data.*;
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
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "${table.tableName}")
@ApiModel(value="${table.name}", description="${table.comment!}")
public class ${table.name} extends ${parentName}Entity<${table.idType.value}> {

    private static final long serialVersionUID = ${serialVersionUID}L;

<#list table.fields as field>
<#--        必填校验-->
    <#if field.createRequire || field.updateRequire>
        <#if field.type.value == "String">@NotBlank<#else>@NotNull</#if>(message = "${field.comment?trim?replace("\"","'")}不能为空", groups = {<#if field.createRequire && field.updateRequire>Insert.class, Update.class<#elseif field.createRequire>Insert.class<#else>Update.class</#if>})
    </#if>
<#--        JsonIgnore-->
    <#if field.jsonIgnore>
    @JsonIgnore
    </#if>
    <#if field.type == "TAGS">
<#--        标签字段-->
    @JsonDeserialize(converter = TagDeserializer.class)
    @JsonSerialize(converter = TagSerializer.class)
    private String ${field.name};

    <#elseif field.type == "DICTIONARY">
<#--        枚举字段-->
    @ApiModelProperty(value = "${field.comment?trim?replace("\"","'")}", dataType="String")
    private ${table.name?cap_first}${field.name?cap_first}Enum ${field.name};

    <#elseif field.type == "MIDDLE_ID">
<#--        关系字段-->
    <#--        一对一-->
        <#if field.relation == 'ONE_TO_ONE'>
    @ApiModelProperty(value = "${field.comment?trim?replace("\"","'")}", dataType="${field.relationEntity!}")
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "${field.relationEntity?uncap_first}_id")
    private ${field.relationEntity} ${field.relationEntity?uncap_first};

    <#--        一对多-->
        <#elseif field.relation == 'ONE_TO_MANY'>
    @ApiModelProperty(value = "${field.comment?trim?replace("\"","'")}", dataType="${field.relationEntity!}")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="${table.name?lower_case}_id")
    private List<${field.relationEntity}> ${field.relationEntity?uncap_first}s = new ArrayList<>();

    <#--        多对多-->
        <#elseif field.relation == 'MANY_TO_MANY'>
    @ApiModelProperty(value = "${field.comment?trim?replace("\"","'")}", dataType="List<${field.relationEntity!}>")
    @ManyToMany(targetEntity = ${field.relationEntity}.class, cascade = CascadeType.DETACH)
    @JoinTable(name = "${module.path?lower_case}_${table.name?lower_case}_${field.relationEntity?lower_case}",
        joinColumns = {@JoinColumn(name = "${table.name?lower_case}_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "${field.relationEntity?lower_case}_id", referencedColumnName = "id")})
    private List<${field.relationEntity}> ${field.relationEntity?uncap_first}s;

    <#--        多对一-->
        <#elseif field.relation == 'MANY_TO_ONE'>
    @ApiModelProperty(value = "${field.comment?trim?replace("\"","'")}", dataType="${field.relationEntity!}")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private ${field.relationEntity} ${field.relationEntity?uncap_first};

        </#if>
    <#else>
<#--        普通字段-->
    @ApiModelProperty(value = "${field.comment?trim?replace("\"","'")}", dataType="${field.type.value}")
        <#if field.type == 'LONG_WORD'>
    @Column(columnDefinition = "text")
        </#if>
        <#if field.type == 'LONG_TEXT'>
    @Column(columnDefinition = "mediumtext")
        </#if>
        <#if field.type == 'HTML'>
    @Column(columnDefinition = "longtext")
        </#if>
    <#--        金钱-->
        <#if field.type == 'MONEY'>
    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
        </#if>
    <#--        时间字段序列化-->
        <#if field.type.value == "LocalDateTime" || field.type.value == "LocalDate" || field.type.value == "LocaTime">
    @JsonDeserialize(using = ${field.type.value}Deserializer.class)
    @JsonSerialize(using = ${field.type.value}Serializer.class)
    @JsonFormat(pattern = "<#if field.type.value == "LocalDateTime">yyyy-MM-dd HH:mm:ss<#elseif field.type.value == "LocalDate">yyyy-MM-dd<#else>HH:mm:ss</#if>")
        </#if>
    private ${field.type.value} ${field.name};

    </#if>
</#list>
}
