package top.keiskeiframework.system.service.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.system.entity.SystemDepartment;
import top.keiskeiframework.system.service.ISystemDepartmentService;

/**
 * <p>
 * 部门管理 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
@ConditionalOnProperty({"keiskei.use-department"})
public class SystemDepartmentServiceImpl extends TreeServiceImpl<SystemDepartment, Long> implements ISystemDepartmentService {



}
