package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.system.entity.Role;
import top.keiskeiframework.system.mapper.RoleMapper;
import top.keiskeiframework.system.service.IRoleService;

/**
 * <p>
 * 角色管理 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Service
public class RoleServiceImpl extends MpListServiceImpl<Role, Integer, RoleMapper> implements IRoleService {


}
