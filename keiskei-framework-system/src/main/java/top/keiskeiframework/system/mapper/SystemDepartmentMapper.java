package top.keiskeiframework.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.keiskeiframework.system.entity.SystemDepartment;

/**
 * <p>
 * 部门管理 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
//@ConditionalOnProperty({"keiskei.use-department"})
public interface SystemDepartmentMapper extends BaseMapper<SystemDepartment> {

}
