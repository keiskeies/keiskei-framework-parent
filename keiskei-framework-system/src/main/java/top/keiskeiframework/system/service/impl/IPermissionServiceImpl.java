package top.keiskeiframework.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.system.entity.Permission;
import top.keiskeiframework.system.repository.PermissionRepository;
import top.keiskeiframework.system.service.IPermissionService;

/**
 * <p>
 * 权限管理 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Service
@ConditionalOnProperty({"keiskei.system.use-permission"})
public class IPermissionServiceImpl extends TreeServiceImpl<Permission> implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


}
