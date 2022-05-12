package top.keiskeiframework.generate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.annotation.OneToMany;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.generate.enums.FieldEnumInfoEffectEnum;
import top.keiskeiframework.generate.enums.FieldEnumInfoTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

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
@TableName(value = "gr_field_enum_info")
@ApiModel(value = "FieldEnumInfo", description = "表字段枚举")
public class FieldEnumInfo extends ListEntity<Integer> {
    private static final long serialVersionUID = -7401234718671320506L;

    @TableId(type = IdType.AUTO)
    private Integer id;

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
    @OneToMany(targetClass = FieldEnumAffectInfo.class, filedName = "fieldEnumId")
    private transient Collection<FieldEnumAffectInfo> fieldEnumAffects;

    private String fieldId;

    @ApiModelProperty(value = "排序", dataType = "Integer")
    @OrderBy
    private Integer sortBy;
}
