package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.IMiddleEntity;
import top.keiskeiframework.common.base.entity.impl.MiddleEntityImpl;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author cjm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_role_permission_relation")
@Entity
@Table(name = "sys_role_permission_relation")
@ApiModel(value = "RolePermission", description = "角色权限关联")
public class RolePermission extends MiddleEntityImpl<Integer, Integer> implements IMiddleEntity<Integer, Integer> {

    private static final long serialVersionUID = 817412605806215848L;

}
