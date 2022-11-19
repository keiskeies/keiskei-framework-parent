package top.keiskeiframework.system.service;

import top.keiskeiframework.common.base.mp.service.IMiddleService;
import top.keiskeiframework.system.entity.UserDepartment;

/**
 * <p>
 * 用户部门
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/15 15:52
 */
public interface IUserDepartmentService extends IMiddleService<UserDepartment, Integer, Integer> {


    /**
     * 更换默认选择组织
     *
     * @param userDepartment 默认选择组织
     */
    void changeDefaultSelectDepartment(UserDepartment userDepartment);
}
