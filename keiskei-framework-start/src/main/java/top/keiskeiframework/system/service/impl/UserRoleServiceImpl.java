package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpMiddleServiceImpl;
import top.keiskeiframework.system.entity.UserRole;
import top.keiskeiframework.system.mapper.UserRoleMapper;
import top.keiskeiframework.system.service.IUserRoleService;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 00:56
 */
@Service
public class UserRoleServiceImpl extends MpMiddleServiceImpl<UserRole, Integer, Integer, UserRoleMapper>
        implements IUserRoleService {
}
