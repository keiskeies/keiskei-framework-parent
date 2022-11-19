package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpTreeServiceImpl;
import top.keiskeiframework.system.entity.Department;
import top.keiskeiframework.system.mapper.DepartmentMapper;
import top.keiskeiframework.system.service.IDepartmentService;

/**
 * <p>
 * 组织管理 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class DepartmentServiceImpl extends MpTreeServiceImpl<Department, Integer, DepartmentMapper> implements IDepartmentService {


}
