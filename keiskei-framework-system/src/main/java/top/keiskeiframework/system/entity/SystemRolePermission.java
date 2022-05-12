package top.keiskeiframework.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.keiskeiframework.common.base.entity.MiddleEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 00:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role_permission")
public class SystemRolePermission extends MiddleEntity<Integer, Integer> {
    private static final long serialVersionUID = -844359160824933761L;


}
