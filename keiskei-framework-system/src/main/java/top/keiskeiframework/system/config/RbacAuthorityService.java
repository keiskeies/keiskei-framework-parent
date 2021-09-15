package top.keiskeiframework.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.enums.SystemEnum;
import top.keiskeiframework.system.entity.Permission;
import top.keiskeiframework.system.entity.Role;
import top.keiskeiframework.system.service.IRoleService;
import top.keiskeiframework.system.vo.user.TokenGrantedAuthority;
import top.keiskeiframework.system.vo.user.TokenUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Rbac权限校验
 *
 * @author 陈加敏
 * @since 2019/7/15 14:25
 */
@Service
@ConditionalOnProperty(value = {"keiskei.system.use-permission"})
public class RbacAuthorityService {

    @Autowired
    private IRoleService roleService;
    /**
     * URL匹配工具
     */
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * 权限判断
     *
     * @param request        .
     * @param authentication .
     * @return .
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object userInfo = authentication.getPrincipal();

        //用户信息判断 , 当token违背携带时出现此情况
        if (!(userInfo instanceof UserDetails)) {
            return false;
        }
        TokenUser tokenUser = (TokenUser) userInfo;
        //超级管理员判断
        if (SystemEnum.SUPER_ADMIN_ID.equals(tokenUser.getId())) {
            return true;
        }

        Collection<TokenGrantedAuthority> authorities = tokenUser.getAuthorities();
        //角色为空判断
        if (CollectionUtils.isEmpty(authorities)) {
            return false;
        }

        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();

        //获取角色权限
        for (TokenGrantedAuthority authority : authorities) {
            Role role = roleService.getById(authority.getId());
            if (CollectionUtils.isEmpty(role.getPermissions())) {
                continue;
            }
            for (Permission permission : role.getPermissions()) {
                if (ANT_PATH_MATCHER.match(permission.getPath(), requestUri) && requestMethod.equalsIgnoreCase(permission.getMethod())) {
                    return true;
                }
            }
        }
        return false;
    }
}