package top.keiskeiframework.system.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.system.dto.UserDTO;
import top.keiskeiframework.system.dto.UserDepartmentDTO;
import top.keiskeiframework.system.dto.UserSettingDTO;
import top.keiskeiframework.system.entity.*;
import top.keiskeiframework.system.enums.SystemEnum;
import top.keiskeiframework.system.mapper.UserMapper;
import top.keiskeiframework.system.properties.SystemProperties;
import top.keiskeiframework.system.service.*;
import top.keiskeiframework.system.util.SecurityUtils;
import top.keiskeiframework.system.vo.TokenDepartment;
import top.keiskeiframework.system.vo.TokenGrantedAuthority;
import top.keiskeiframework.system.vo.TokenUser;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理员ServiceImpl
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020年12月10日11:41:05
 */
@Service
public class UserServiceImpl extends MpListServiceImpl<User, Integer, UserMapper> implements IUserService {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;
    @Autowired
    private SystemProperties systemProperties;
    @Autowired
    private IUserPasswordService userPasswordService;
    @Autowired
    private IUserSettingService userSettingService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IUserDepartmentService userDepartmentService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IUserLoginRecordService userLoginRecordService;
    @Autowired
    @Lazy
    private IUserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new BizException(BizExceptionEnum.CHECK_FIELD);
        }
        User user = this.findOneByColumn(User::getUsername, username);

        if (null == user) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        UserPassword userPassword = userPasswordService.findOneByColumn(UserPassword::getUserId, user.getId());

        TokenUser tokenUser = new TokenUser();

        BeanUtils.copyProperties(user, tokenUser);
        tokenUser.setPassword(userPassword.getPassword());
        tokenUser.setId(user.getId());


        LocalDateTime now = LocalDateTime.now();
        // 账号是否过期
        tokenUser.setAccountNonExpired(null == user.getAccountExpiredTime() ||
                user.getAccountExpiredTime().isBefore(now));
        // 账号是否被锁
        tokenUser.setAccountNonLocked(null == user.getAccountLockTime() ||
                user.getAccountLockTime().isBefore(now));
        // 账号密码是否需要修改
        tokenUser.setCredentialsNonExpired(userPassword.getFirstSet() ||
                userPassword.getCreateTime().isAfter(now.plusDays(-1L * systemProperties.getPasswordExpiredDay())));
        return tokenUser;
    }

    @Override
    public boolean save(User user) {
        super.save(user);
        // 初始化密码
        userPasswordService.initPassword(user.getId());

        return Boolean.TRUE;
    }

    @Override
    public Long addPasswordErrorTimes(String username) {
        if (StringUtils.isEmpty(username)) {
            return 0L;
        }
        User user = this.findOneByColumn(User::getUsername, username);
        if (null == user) {
            return 0L;
        }
        String overTimeKey = String.format(SystemEnum.USER_ERROR_TIMES_SUFFIX, username);
        Long passwordErrorTimes = redisTemplate.opsForValue().increment(overTimeKey);
        if (null != passwordErrorTimes) {
            if (passwordErrorTimes >= systemProperties.getPasswordErrorTimesMax()) {
                LocalDateTime lockTime = LocalDateTime.now().plusMinutes(systemProperties.getLockMinutes());
                user.setAccountLockTime(lockTime);
                userService.updateOne(user);
                redisTemplate.delete(overTimeKey);
            }
        }
        return passwordErrorTimes;
    }

    @Override
    public UserDTO getSelfInfo() {

        TokenUser tokenUser = SecurityUtils.getSessionUser();
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(tokenUser, userDto);

        User user = userService.findOneById(tokenUser.getId());

        if (!CollectionUtils.isEmpty(user.getDepartments())) {

            List<TokenDepartment> departments = user.getDepartments().stream()
                    .map(e -> new TokenDepartment(e.getId(), e.getName())).collect(Collectors.toList());
            userDto.setDepartments(departments);
        }

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            Set<TokenGrantedAuthority> authorities = new HashSet<>();
            Set<String> permissions = new HashSet<>();

            for (Role role : user.getRoles()) {
                authorities.add(new TokenGrantedAuthority(role.getId(), role.getName()));
                permissions.addAll(role.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toSet()));
            }


            userDto.setAuthorities(authorities);
            userDto.setPermissions(permissions);
        }

        UserSetting userSetting = userSettingService.findOneById(tokenUser.getId());
        UserSettingDTO userSettingDTO = new UserSettingDTO();
        BeanUtils.copyProperties(userSetting, userSettingDTO);
        userDto.setSetting(userSettingDTO);

        return userDto;

    }

    @Override
    public TokenUser confirmDepartment(TokenUser tokenUser, UserDepartmentDTO userDepartment) {
        if (SystemEnum.SUPER_ADMIN_ID != tokenUser.getId()) {
            List<UserDepartment> userDepartments = userDepartmentService.getById1(tokenUser.getId());
            if (!CollectionUtils.isEmpty(userDepartments)) {
                BaseRequestVO<Department, Integer> baseRequestVO = new BaseRequestVO<>();
                List<Integer> departmentIds = userDepartments.stream().map(UserDepartment::getId2).collect(Collectors.toList());
                baseRequestVO.addCondition(new QueryConditionVO("id", ConditionEnum.IN, departmentIds));
                List<Department> departments = departmentService.findListByCondition(baseRequestVO);
                if (!CollectionUtils.isEmpty(departments)) {
                    List<TokenDepartment> tokenDepartments = departments.stream().map(e -> new TokenDepartment(e.getId(), e.getName())).collect(Collectors.toList());
                    tokenUser.setDepartments(tokenDepartments);

                    String departmentSign = departments.get(0).getSign();

                    if (null == userDepartment || null == userDepartment.getId()) {
                        // 组装默认部门
                        Optional<UserDepartment> d = userDepartments.stream().filter(UserDepartment::getDefaultSelect).findFirst();
                        if (d.isPresent()) {
                            Integer departmentId = d.get().getId2();
                            Optional<Department> defaultDepartment = departments.stream().filter(e -> e.getId().equals(departmentId)).findFirst();
                            if (defaultDepartment.isPresent()) {
                                departmentSign = defaultDepartment.get().getSign();
                            }
                        }
                    } else {
                        Optional<Department> d = departments.stream().filter(e -> e.getId().equals(userDepartment.getId())).findFirst();
                        if (d.isPresent()) {
                            departmentSign = d.get().getSign();

                            userDepartments = userDepartments.stream().peek(e -> {
                                e.setDefaultSelect(e.getId2().equals(userDepartment.getId()));
                            }).collect(Collectors.toList());

                            userDepartmentService.updateById1(userDepartments);

                        } else {
                            throw new BizException(BizExceptionEnum.AUTH_FORBIDDEN);
                        }
                    }


                    tokenUser.setDepartment(departmentSign);
                }
            }
        }

        return tokenUser;
    }

    @Override
    public TokenUser confirmRole(TokenUser tokenUser) {
        if (SystemEnum.SUPER_ADMIN_ID != tokenUser.getId()) {
            List<UserRole> userRoles = userRoleService.getById1(tokenUser.getId());
            if (!CollectionUtils.isEmpty(userRoles)) {
                BaseRequestVO<Role, Integer> baseRequestVO = new BaseRequestVO<>();
                List<Integer> roleIds = userRoles.stream().map(UserRole::getId2).collect(Collectors.toList());
                baseRequestVO.addCondition(new QueryConditionVO("id", ConditionEnum.IN, roleIds));
                List<Role> roles = roleService.findListByCondition(baseRequestVO);
                if (!CollectionUtils.isEmpty(roles)) {
                    List<TokenGrantedAuthority> authorities = roles.stream().map(e -> new TokenGrantedAuthority(e.getId(), e.getName())).collect(Collectors.toList());
                    tokenUser.setAuthorities(authorities);
                }
            }
        } else {
            tokenUser.setAuthorities(Collections.singletonList(new TokenGrantedAuthority(SystemEnum.SUPER_ADMIN_ID, "顶级管理员")));
        }
        return tokenUser;
    }

    @Override
    public void addLoginRecord(HttpServletRequest request, TokenUser tokenUser) {
        UserLoginRecord record = new UserLoginRecord();
        record.setIp(SecurityUtils.getIpAddress(request));
        record.setUserId(tokenUser.getId());
        userLoginRecordService.saveOne(record);
    }
}
