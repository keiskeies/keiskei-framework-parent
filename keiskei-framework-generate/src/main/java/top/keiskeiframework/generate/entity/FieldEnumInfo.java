package top.keiskeiframework.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 字段枚举
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/23 15:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gr_field_enum_info")
@ApiModel(value = "FieldEnumInfo", description = "表字段枚举")
public class FieldEnumInfo extends BaseEntity {
    private static final long serialVersionUID = -7401234718671320506L;

    @ApiModelProperty(value = "所属字段", dataType = "Long")
    @NotNull(message = "所属字段不能为空", groups = {Insert.class})
    private Long fieldId;

    @ApiModelProperty(value = "CODE", dataType = "String")
    @NotNull(message = "CODE不能为空", groups = {Insert.class})
    private String code;

    @ApiModelProperty(value = "名称", dataType = "String")
    @NotBlank(message = "名称不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "类型", dataType = "String")
    @NotBlank(message = "类型不能为空", groups = {Insert.class})
    private String type;

    @ApiModelProperty(value = "主题", dataType = "String")
    @NotBlank(message = "主题不能为空", groups = {Insert.class})
    private String effect;
}
