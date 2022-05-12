package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.annotation.ManyToMany;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * <p>
 * 角色信息
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019-06-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_role")
@ApiModel(value = "SystemRole", description = "角色")
public class SystemRole extends ListEntity<Integer> {
    private static final long serialVersionUID = -6932146634496116207L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色名称", dataType = "String")
    @NotBlank(message = "角色名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ManyToMany(middleClass = SystemRolePermission.class, targetClass = SystemPermission.class)
    private transient Collection<SystemPermission> systemPermissions;
}
