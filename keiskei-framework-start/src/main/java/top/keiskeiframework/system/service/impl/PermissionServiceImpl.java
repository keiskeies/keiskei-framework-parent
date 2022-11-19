package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpTreeServiceImpl;
import top.keiskeiframework.system.entity.Permission;
import top.keiskeiframework.system.mapper.PermissionMapper;
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
public class PermissionServiceImpl extends MpTreeServiceImpl<Permission, Integer, PermissionMapper> implements IPermissionService {


}
