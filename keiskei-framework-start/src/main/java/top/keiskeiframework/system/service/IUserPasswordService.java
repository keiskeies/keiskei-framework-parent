package top.keiskeiframework.system.service;

import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.system.dto.UserPasswordDTO;
import top.keiskeiframework.system.entity.UserPassword;

/**
 * <p>
 * 用户密码
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/15 15:54
 */
public interface IUserPasswordService extends IBaseService<UserPassword, Integer> {


    /**
     * 初始化密码
     *
     * @param userId 用户id
     */
    void initPassword(Integer userId);

    /**
     * 更新密码
     *
     * @param userId       用户ID
     * @param userPassword 密码实体类
     */
    void updatePassword(Integer userId, UserPasswordDTO userPassword);
}
