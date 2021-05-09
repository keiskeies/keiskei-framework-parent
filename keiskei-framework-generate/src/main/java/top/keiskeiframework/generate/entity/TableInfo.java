package top.keiskeiframework.generate.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 表结构信息
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
@Table(name = "gr_table_info")
@ApiModel(value = "TableInfo", description = "表结构信息")
public class TableInfo extends BaseEntity {

    private static final long serialVersionUID = 7715195221883078519L;

    @ApiModelProperty(value = "所属模块", dataType = "Long")
    @NotNull(message = "所属模块不能为空", groups = {Insert.class})
    private Long moduleId;

    @ApiModelProperty(value = "实体类名", dataType = "String")
    @NotBlank(message = "实体类名不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "表注释", dataType = "String")
    @NotBlank(message = "表注释不能为空", groups = {Insert.class})
    private String comment;

    @ApiModelProperty(value = "表名称", dataType = "String")
    @NotBlank(message = "表名称不能为空", groups = {Insert.class})
    private String table;

    @ApiModelProperty(value = "表类型", dataType = "String")
    private String type;

    @ApiModelProperty(value = "是否构建接口", dataType = "Boolean")
    private Boolean buildController;

    @Transient
    private List<FieldInfo> fields;

}