package top.keiskeiframework.system.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.cache.serivce.CacheStorageService;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.SystemEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.SecurityUtils;
import top.keiskeiframework.common.vo.user.TokenGrantedAuthority;
import top.keiskeiframework.common.vo.user.TokenUser;
import top.keiskeiframework.system.dto.UserDto;
import top.keiskeiframework.system.entity.Department;
import top.keiskeiframework.system.entity.Permission;
import top.keiskeiframework.system.entity.Role;
import top.keiskeiframework.system.entity.User;
import top.keiskeiframework.system.properties.SystemProperties;
import top.keiskeiframework.system.repository.UserRepository;
import top.keiskeiframework.system.service.IDepartmentService;
import top.keiskeiframework.system.service.IPermissionService;
import top.keiskeiframework.system.service.IUserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 管理员ServiceImpl
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020年12月10日11:41:05
 */
@Service
public class IUserServiceImpl extends ListServiceImpl<User, Long> implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CacheStorageService cacheStorageService;
    @Autowired
    private SystemProperties systemProperties;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IPermissionService permissionService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new BizException(BizExceptionEnum.CHECK_FIELD);
        }
        User user = userRepository.findTopByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        TokenUser tokenUser = new TokenUser();
        BeanUtils.copyProperties(user, tokenUser);
        tokenUser.setId(user.getId());
        if (SystemEnum.SUPER_ADMIN_ID != tokenUser.getId()) {

            Department department = departmentService.getById(tokenUser.getDepartmentId());
            Assert.notNull(department, BizExceptionEnum.AUTH_ACCOUNT_EXPIRED.getMsg());

            tokenUser.setDepartment(department.getSign());

            LocalDateTime now = LocalDateTime.now();
            tokenUser.setAccountNonExpired(null != user.getAccountExpiredTime() && user.getAccountExpiredTime().isAfter(now));
            tokenUser.setAccountNonLocked(null == user.getAccountLockTime() || user.getAccountLockTime().isBefore(now));
            tokenUser.setCredentialsNonExpired(null != user.getLastModifyPasswordTime() && user.getLastModifyPasswordTime().isAfter(now.plusDays(-30)));


            List<TokenGrantedAuthority> authorities = user.getRoles().stream().map(e -> new TokenGrantedAuthority(e.getId(), e.getName())).collect(Collectors.toList());
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
        User user = userRepository.findTopByUsername(username);
        if (null == user) {
            return;
        }
        if (cacheStorageService.overTimeNum(String.format(SystemEnum.USER_ERROR_TIMES_SUFFIX, username))) {
            LocalDateTime lockTime = LocalDateTime.now().plusMinutes(systemProperties.getLockMinutes());
            user.setAccountLockTime(lockTime);
            userRepository.save(user);
        }
    }

    @Override
    public UserDto getSelfInfo() {
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(tokenUser, userDto);

        if (SystemEnum.SUPER_ADMIN_ID == tokenUser.getId()) {
            List<Permission> permissions = permissionService.options();
            userDto.setPermissions(permissions.stream().map(Permission::getPermission).collect(Collectors.toList()));
            return userDto;
        }

        User user = userRepository.getOne(tokenUser.getId());
        Set<Permission> permissions = new HashSet<>();

        for (Role role : user.getRoles()) {
            permissions.addAll(role.getPermissions());
        }

        userDto.setPermissions(permissions.stream().map(Permission::getPermission).collect(Collectors.toList()));
        return userDto;
    }
}
