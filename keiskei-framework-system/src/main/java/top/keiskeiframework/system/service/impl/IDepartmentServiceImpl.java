package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.system.entity.Department;
import top.keiskeiframework.system.service.IDepartmentService;

/**
 * <p>
 * 部门管理 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class IDepartmentServiceImpl extends TreeServiceImpl<Department, Long> implements IDepartmentService {



}
