package top.keiskeiframework.system.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.system.dto.UserDTO;
import top.keiskeiframework.system.dto.UserDepartmentDTO;
import top.keiskeiframework.system.entity.User;
import top.keiskeiframework.system.vo.TokenUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员Service
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020年12月10日11:41:05
 */
public interface IUserService extends IBaseService<User, Integer>, UserDetailsService {

    /**
     * 添加用户密码错误次数
     *
     * @param username 用户名
     */
    void addPasswordErrorTimes(String username);

    /**
     * 获取当前用户信息
     *
     * @return .
     */
    UserDTO getSelfInfo();

    /**
     * 更换数据部门
     * 更换查询的部门权限
     *
     * @param userDepartment 数据部门
     * @param tokenUser      用户
     * @return 。
     */
    TokenUser confirmDepartment(TokenUser tokenUser, UserDepartmentDTO userDepartment);

    /**
     * 组装用户角色数据
     *
     * @param tokenUser 用户
     * @return 。
     */
    TokenUser confirmRole(TokenUser tokenUser);

    /**
     * 添加登录记录
     *
     * @param tokenUser 用户
     * @param request   HttpServletRequest
     */
    void addLoginRecord(HttpServletRequest request, TokenUser tokenUser);

}
