package top.keiskeiframework.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.generate.enums.FieldEnumInfoEffectEnum;
import top.keiskeiframework.generate.enums.FieldEnumInfoTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
@Document("gr_field_enum_info")
@ApiModel(value = "FieldEnumInfo", description = "表字段枚举")
public class FieldEnumInfo extends ListEntity {
    private static final long serialVersionUID = -7401234718671320506L;

    @ApiModelProperty(value = "名称", dataType = "String")
    @NotBlank(message = "名称不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "comment", dataType = "String")
    @NotBlank(message = "注释不能为空", groups = {Insert.class})
    private String comment;

    @ApiModelProperty(value = "类型", dataType = "String")
    @NotNull(message = "类型不能为空", groups = {Insert.class})
    private FieldEnumInfoTypeEnum type;

    @ApiModelProperty(value = "主题", dataType = "String")
    @NotNull(message = "主题不能为空", groups = {Insert.class})
    private FieldEnumInfoEffectEnum effect;

    @ApiModelProperty(value = "枚举影响值", dataType = "List<FieldEnumAffectInfo>")
    @DBRef(lazy = true)
    private List<FieldEnumAffectInfo> fieldEnumAffects;

    @ApiModelProperty(value = "排序", dataType = "Integer")
    private Integer sortBy;
}
