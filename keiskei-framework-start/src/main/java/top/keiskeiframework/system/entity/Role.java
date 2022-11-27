package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.entity.impl.ListEntityImpl;
import top.keiskeiframework.common.base.mp.annotation.MpManyToMany;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @author cjm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_role")
@TableName(value = "sys_role")
@ApiModel(value = "Role", description = "角色")
public class Role extends ListEntityImpl<Integer> implements IListEntity<Integer> {

    private static final long serialVersionUID = -8676268982933874117L;

    @ApiModelProperty(value = "角色名称", dataType = "String")
    @NotBlank(message = "角色名称不能为空", groups = {Insert.class, Update.class})
    private String name;

    @MpManyToMany(middleClass = RolePermission.class, targetClass = Permission.class)
    @TableField(exist = false)
    private transient List<Permission> permissions;

}
