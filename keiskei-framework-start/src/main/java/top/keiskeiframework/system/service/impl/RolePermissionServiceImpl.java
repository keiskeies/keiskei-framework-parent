package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpMiddleServiceImpl;
import top.keiskeiframework.system.entity.RolePermission;
import top.keiskeiframework.system.mapper.RolePermissionMapper;
import top.keiskeiframework.system.service.IRolePermissionService;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 00:56
 */
@Service
public class RolePermissionServiceImpl extends MpMiddleServiceImpl<RolePermission, Integer, Integer, RolePermissionMapper>
        implements IRolePermissionService {
}
