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
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.annotation.OneToMany;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.generate.enums.FieldInfoRelationEnum;
import top.keiskeiframework.generate.enums.FieldInfoTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

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
@TableName(value = "gr_field_info")
@ApiModel(value = "FieldInfo", description = "表字段信息")
public class FieldInfo extends ListEntity<Integer> {

    private static final long serialVersionUID = -6407989526318566170L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "字段名称", dataType = "String")
    @NotBlank(message = "字段名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ApiModelProperty(value = "字段注释", dataType = "String")
    @NotBlank(message = "字段注释不能为空", groups = {Insert.class, Update.class})
    private String comment;

    @ApiModelProperty(value = "字段解释", dataType = "String")
    private String tooltip;

    @ApiModelProperty(value = "字段类型", dataType = "String")
    @NotNull(message = "字段类型不能为空", groups = {Insert.class, Update.class})
    private FieldInfoTypeEnum type;

    @ApiModelProperty(value = "新增必填", dataType = "Boolean")
    private Boolean createRequire = Boolean.TRUE;

    @ApiModelProperty(value = "更新必填", dataType = "Boolean")
    private Boolean updateRequire = Boolean.TRUE;

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

    @ApiModelProperty(value = "表格宽度", dataType = "Integer")
    private Integer tableWidth = 200;

    @ApiModelProperty(value = "字段校验", dataType = "String")
    private String validate;

    @OneToMany(filedName = "fieldId", targetClass = FieldEnumInfo.class)
    private transient Collection<FieldEnumInfo> fieldEnums;

    private String tableId;

    @ApiModelProperty(value = "排序", dataType = "ID")
    @OrderBy
    private Integer sortBy;


}
