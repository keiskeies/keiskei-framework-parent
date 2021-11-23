package top.keiskeiframework.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 字段枚举值影响
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/5/29 0:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gr_field_enum_affect_info")
@ApiModel(value = "FieldEnumInfo", description = "表字段枚举影响值")
public class FieldEnumAffectInfo extends ListEntity<Long> {
    private static final long serialVersionUID = 8981895417058350169L;

    @ApiModelProperty(value = "字段名称", dataType = "String")
    @NotBlank(message = "字段不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "清除字段", dataType = "Boolean")
    private Boolean cleanValue;

    @ApiModelProperty(value = "字段值", dataType = "String")
    private String value;

    @ApiModelProperty(value = "禁用字段", dataType = "Boolean")
    private Boolean disableEdit;

    @ApiModelProperty(value = "排序", dataType = "Integer")
    @SortBy(desc = false)
    private Long sortBy;


    @PostPersist
    private void postPersist() {
        this.sortBy = super.getId();
    }
}
