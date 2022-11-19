package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.keiskeiframework.common.base.entity.impl.MiddleEntityImpl;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author cjm
 */
@Data
@Entity
@Table(name = "sys_user_role_relation")
@TableName(value = "sys_user_role_relation")
@ApiModel(value = "UserRole", description = "用户角色")
public class UserRole extends MiddleEntityImpl<Integer, Integer> {

    private static final long serialVersionUID = 5924507988013806269L;
}
