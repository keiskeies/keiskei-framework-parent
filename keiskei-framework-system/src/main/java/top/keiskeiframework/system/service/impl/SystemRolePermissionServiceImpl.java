package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.MiddleServiceImpl;
import top.keiskeiframework.system.entity.SystemRolePermission;
import top.keiskeiframework.system.mapper.SystemRolePermissionMapper;
import top.keiskeiframework.system.service.ISystemRolePermissionService;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 00:56
 */
@Service
public class SystemRolePermissionServiceImpl extends MiddleServiceImpl<SystemRolePermission, Integer, Integer, SystemRolePermissionMapper>
        implements ISystemRolePermissionService {
}
