package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_role")
@ApiModel(value = "SystemRole", description = "角色")
public class SystemRole extends ListEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "角色名称", dataType = "String")
    @NotBlank(message = "角色名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @ManyToMany
    @JoinTable(name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private transient Collection<SystemPermission> systemPermissions;
}
