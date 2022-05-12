package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.MiddleServiceImpl;
import top.keiskeiframework.system.entity.SystemUserRole;
import top.keiskeiframework.system.mapper.SystemUserRoleMapper;
import top.keiskeiframework.system.service.ISystemUserRoleService;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 00:56
 */
@Service
public class SystemUserRoleServiceImpl extends MiddleServiceImpl<SystemUserRole, Integer, Integer, SystemUserRoleMapper>
        implements ISystemUserRoleService {
}
