package top.keiskeiframework.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.base.entity.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 部门管理
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
@Table(name = "sys_department")
@ApiModel(value = "SystemDepartment", description = "部门管理")
public class SystemDepartment extends TreeEntity<Long> {

    private static final long serialVersionUID = 4845168322937249454L;

    @ApiModelProperty(value = "部门名称", dataType = "String")
    @NotBlank(message = "部门名称不能为空", groups = {Insert.class})
    private String name;

}
