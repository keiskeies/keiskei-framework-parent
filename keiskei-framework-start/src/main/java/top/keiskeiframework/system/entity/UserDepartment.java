package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.IMiddleEntity;
import top.keiskeiframework.common.base.entity.impl.MiddleEntityImpl;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 * 用户组织
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/15 15:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user_department")
@TableName(value = "sys_user_department")
@ApiModel(value = "UserDepartment", description = "用户组织")
public class UserDepartment extends MiddleEntityImpl<Integer, Integer> implements IMiddleEntity<Integer, Integer> {

    private static final long serialVersionUID = 4743420366917811994L;

    @ApiModelProperty(value = "默认选择", dataType = "Boolean")
    private Boolean defaultSelect;
}
