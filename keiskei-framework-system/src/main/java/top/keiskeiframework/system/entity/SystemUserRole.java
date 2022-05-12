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
 * @since 2022/5/13 01:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user_role")
public class SystemUserRole extends MiddleEntity<Integer, Integer> {
    private static final long serialVersionUID = 8501417459478968431L;
}
