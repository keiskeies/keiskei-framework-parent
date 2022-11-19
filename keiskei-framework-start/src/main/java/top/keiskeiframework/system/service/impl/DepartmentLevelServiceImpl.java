package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.system.entity.DepartmentLevel;
import top.keiskeiframework.system.mapper.DepartmentLevelMapper;
import top.keiskeiframework.system.service.IDepartmentLevelService;

/**
 * <p>
 * 角色管理 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Service
public class DepartmentLevelServiceImpl extends MpListServiceImpl<DepartmentLevel, Integer, DepartmentLevelMapper> implements IDepartmentLevelService {


}
