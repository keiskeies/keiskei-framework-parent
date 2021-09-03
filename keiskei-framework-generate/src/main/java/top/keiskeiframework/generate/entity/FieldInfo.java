package top.keiskeiframework.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.BaseLongIdEntity;
import top.keiskeiframework.generate.enums.FieldInfoTypeEnum;
import top.keiskeiframework.generate.enums.FieldInfoRelationEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 表字段信息
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gr_field_info")
@ApiModel(value = "FieldInfo", description = "表字段信息")
public class FieldInfo extends BaseLongIdEntity {

    private static final long serialVersionUID = -6407989526318566170L;

    @ApiModelProperty(value = "字段名称", dataType = "String")
    @NotBlank(message = "字段名称不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "字段注释", dataType = "String")
    @NotBlank(message = "字段注释不能为空", groups = {Insert.class})
    private String comment;

    @ApiModelProperty(value = "字段解释", dataType = "String")
    @NotBlank(message = "字段解释不能为空", groups = {Insert.class})
    private String tooltip;

    @ApiModelProperty(value = "字段类型", dataType = "String")
    private FieldInfoTypeEnum type;

    @ApiModelProperty(value = "新增必填", dataType = "Boolean")
    private Boolean createRequire = Boolean.TRUE;

    @ApiModelProperty(value = "更新必填", dataType = "Boolean")
    private Boolean updateRequire = Boolean.TRUE;

    @ApiModelProperty(value = "字段校验", dataType = "String")
    private String validate;

    @ApiModelProperty(value = "字段关系", dataType = "String")
    private FieldInfoRelationEnum relation;

    @ApiModelProperty(value = "关系关联表", dataType = "String")
    private String relationEntity;

    @ApiModelProperty(value = "是否查询字段", dataType = "Boolean")
    private Boolean queryAble = Boolean.FALSE;

    @ApiModelProperty(value = "是否不返回页面", dataType = "Boolean")
    private Boolean jsonIgnore = Boolean.FALSE;

    @ApiModelProperty(value = "是否直接展示", dataType = "Boolean")
    private Boolean directShow = Boolean.TRUE;

    @ApiModelProperty(value = "是否可编辑", dataType = "Boolean")
    private Boolean editAble = Boolean.TRUE;

    @ApiModelProperty(value = "是否可排序", dataType = "Boolean")
    private Boolean sortAble = Boolean.FALSE;

    @ApiModelProperty(value = "表格宽度", dataType = "Long")
    private Integer tableWidth = 200;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "field_id")
    private List<FieldEnumInfo> fieldEnums;

}
