package top.keiskeiframework.system.service;

import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.system.dto.UserDto;
import top.keiskeiframework.system.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 管理员Service
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020年12月10日11:41:05
 */
public interface IUserService extends BaseService<User, Long>, UserDetailsService {

    /**
     * 添加用户密码错误次数
     * @param username 用户名
     */
    void addPasswordErrorTimes(String username);

    /**
     * 获取当前用户信息
     * @return .
     */
    UserDto getSelfInfo();


}