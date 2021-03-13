package top.keiskeiframework.system.service.impl;

import top.keiskeiframework.common.base.service.impl.BaseServiceImpl;
import top.keiskeiframework.system.entity.Role;
import top.keiskeiframework.system.repository.RoleRepository;
import top.keiskeiframework.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色管理 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Service
public class IRoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

}
