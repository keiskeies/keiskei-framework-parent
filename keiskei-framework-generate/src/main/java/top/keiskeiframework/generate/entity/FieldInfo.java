package top.keiskeiframework.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class FieldInfo extends ListEntity {

    private static final long serialVersionUID = -6407989526318566170L;

    @ApiModelProperty(value = "所属表", dataType = "Long")
    @NotNull(message = "所属表不能为空", groups = {Insert.class})
    private Long tableId;

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
    @NotBlank(message = "字段类型不能为空", groups = {Insert.class})
    private String type;

    @ApiModelProperty(value = "新增必填", dataType = "Boolean")
    @NotNull(message = "新增必填不能为空", groups = {Insert.class})
    private Boolean createRequire = Boolean.TRUE;

    @ApiModelProperty(value = "更新必填", dataType = "Boolean")
    @NotNull(message = "更新必填不能为空", groups = {Insert.class})
    private Boolean updateRequire = Boolean.TRUE;

    @ApiModelProperty(value = "多对多", dataType = "String")
    @NotBlank(message = "多对多不能为空", groups = {Insert.class})
    private String manyToMany;

    @ApiModelProperty(value = "一对一", dataType = "String")
    @NotBlank(message = "一对一不能为空", groups = {Insert.class})
    private String oneToOne;

    @ApiModelProperty(value = "是否查询字段", dataType = "Boolean")
    @NotNull(message = "是否查询字段不能为空", groups = {Insert.class})
    private Boolean queryColumn = Boolean.FALSE;

    @ApiModelProperty(value = "是否不返回页面", dataType = "Boolean")
    private Boolean jsonIgnore = Boolean.FALSE;

    @ApiModelProperty(value = "是否直接展示", dataType = "Boolean")
    private Boolean show = Boolean.TRUE;

    @ApiModelProperty(value = "是否可编辑", dataType = "Boolean")
    private Boolean edit = Boolean.TRUE;

    @ApiModelProperty(value = "是否可排序", dataType = "Boolean")
    private Boolean sortable = Boolean.FALSE;

    @ApiModelProperty(value = "表格宽度", dataType = "Long")
    private Long tableWidth = 200L;

    @Transient
    private List<FieldEnumInfo> fieldEnums;

}
