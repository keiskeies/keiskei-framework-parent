package top.keiskeiframework.generate.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import top.keiskeiframework.common.annotation.validate.Insert;

import java.util.List;

/**
 * <p>
 * 模块信息
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
@Table(name = "gr_module_info")
@ApiModel(value = "ModuleInfo", description = "模块信息")
public class ModuleInfo extends BaseEntity {

    private static final long serialVersionUID = 754302484437506602L;

    @ApiModelProperty(value = "所属项目", dataType = "Long")
    @NotNull(message = "所属项目不能为空", groups = {Insert.class})
    private Long itemId;

    @ApiModelProperty(value = "模块名称", dataType = "String")
    @NotBlank(message = "模块名称不能为空", groups = {Insert.class})
    private String name;

    @ApiModelProperty(value = "模块注释", dataType = "String")
    @NotBlank(message = "模块注释不能为空", groups = {Insert.class})
    private String comment;

    @ApiModelProperty(value = "模块包名", dataType = "String")
    @NotBlank(message = "模块包名不能为空", groups = {Insert.class})
    private String packageName;

    @Transient
    private List<TableInfo> tables;
}
