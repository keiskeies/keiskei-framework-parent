package top.keiskeiframework.system.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.base.enums.SystemEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.system.dto.SystemUserDto;
import top.keiskeiframework.system.entity.SystemDepartment;
import top.keiskeiframework.system.entity.SystemPermission;
import top.keiskeiframework.system.entity.SystemRole;
import top.keiskeiframework.system.entity.SystemUser;
import top.keiskeiframework.system.mapper.SystemUserMapper;
import top.keiskeiframework.system.properties.SystemProperties;
import top.keiskeiframework.system.service.ISystemDepartmentService;
import top.keiskeiframework.system.service.ISystemPermissionService;
import top.keiskeiframework.system.service.ISystemUserService;
import top.keiskeiframework.system.util.SecurityUtils;
import top.keiskeiframework.system.vo.TokenGrantedAuthority;
import top.keiskeiframework.system.vo.TokenUser;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 管理员ServiceImpl
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020年12月10日11:41:05
 */
@Service
public class SystemUserServiceImpl extends ListServiceImpl<SystemUser, Integer, SystemUserMapper> implements ISystemUserService {
    @Autowired
    private SystemProperties systemProperties;
    @Autowired(required = false)
    private ISystemDepartmentService departmentService;
    @Autowired(required = false)
    private ISystemPermissionService permissionService;
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new BizException(BizExceptionEnum.CHECK_FIELD);
        }
        SystemUser systemUser = this.findOneByColumn("username", username);
        if (null == systemUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        TokenUser tokenUser = new TokenUser();
        BeanUtils.copyProperties(systemUser, tokenUser);
        tokenUser.setId(systemUser.getId());
        if (SystemEnum.SUPER_ADMIN_ID != tokenUser.getId()) {

            if (null != departmentService) {
                SystemDepartment systemDepartment = departmentService.findOneById(tokenUser.getDepartmentId());
                Assert.notNull(systemDepartment, BizExceptionEnum.AUTH_ACCOUNT_EXPIRED.getMsg());
                tokenUser.setDepartment(systemDepartment.getSign());
            }


            LocalDateTime now = LocalDateTime.now();
            tokenUser.setAccountNonExpired(null != systemUser.getAccountExpiredTime() && systemUser.getAccountExpiredTime().isAfter(now));
            tokenUser.setAccountNonLocked(null == systemUser.getAccountLockTime() || systemUser.getAccountLockTime().isBefore(now));
            tokenUser.setCredentialsNonExpired(null != systemUser.getLastModifyPasswordTime() && systemUser.getLastModifyPasswordTime().isAfter(now.plusDays(-30)));


            List<TokenGrantedAuthority> authorities = systemUser.getSystemRoles().stream().map(e -> new TokenGrantedAuthority(e.getId(), e.getName())).collect(Collectors.toList());
            tokenUser.setAuthorities(authorities);
        } else {
            tokenUser.setAuthorities(Collections.singletonList(new TokenGrantedAuthority(SystemEnum.SUPER_ADMIN_ID, "顶级管理员")));
        }


        return tokenUser;
    }

    @Override
    public void addPasswordErrorTimes(String username) {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        SystemUser systemUser = this.findOneByColumn("username", username);
        if (null == systemUser) {
            return;
        }
        String key = String.format(SystemEnum.USER_ERROR_TIMES_SUFFIX, username);

        redisTemplate.opsForValue().setIfAbsent(key, 0, 5, TimeUnit.MINUTES);
        Long times = redisTemplate.opsForValue().increment(key, 1L);
        if (times == null) {
            redisTemplate.opsForValue().setIfAbsent(key, 1, 5, TimeUnit.MINUTES);
            times = 1L;
        }

        if (times >= 5) {
            LocalDateTime lockTime = LocalDateTime.now().plusMinutes(systemProperties.getLockMinutes());
            systemUser.setAccountLockTime(lockTime);
            super.save(systemUser);
        }
    }

    @Override
    public SystemUserDto getSelfInfo() {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        SystemUserDto systemUserDto = new SystemUserDto();
        BeanUtils.copyProperties(tokenUser, systemUserDto);

        if (SystemEnum.SUPER_ADMIN_ID == tokenUser.getId()) {
            if (null != permissionService) {
                List<SystemPermission> systemPermissions = permissionService.findList();
                systemUserDto.setPermissions(systemPermissions.stream().map(SystemPermission::getPermission).collect(Collectors.toList()));
                return systemUserDto;
            }
        }

        SystemUser systemUser = this.getById(tokenUser.getId());
        Set<SystemPermission> systemPermissions = new HashSet<>();

        for (SystemRole systemRole : systemUser.getSystemRoles()) {
            systemPermissions.addAll(systemRole.getSystemPermissions());
        }

        systemUserDto.setPermissions(systemPermissions.stream().map(SystemPermission::getPermission).collect(Collectors.toList()));
        return systemUserDto;
    }
}
