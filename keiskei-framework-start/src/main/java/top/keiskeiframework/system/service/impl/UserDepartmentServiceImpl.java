package top.keiskeiframework.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpMiddleServiceImpl;
import top.keiskeiframework.system.entity.UserDepartment;
import top.keiskeiframework.system.mapper.UserDepartmentMapper;
import top.keiskeiframework.system.service.IUserDepartmentService;

import java.util.List;

/**
 * <p>
 * 用户部门
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/15 15:55
 */
@Service
public class UserDepartmentServiceImpl extends MpMiddleServiceImpl<UserDepartment, Integer, Integer, UserDepartmentMapper> implements IUserDepartmentService {

    @Autowired
    @Lazy
    private IUserDepartmentService userDepartmentService;

    @Override
    public void changeDefaultSelectDepartment(UserDepartment userDepartment) {
        List<UserDepartment> userDepartments = userDepartmentService.getById1(userDepartment.getId1());
        for (UserDepartment item : userDepartments) {
            if (userDepartment.getId2().equals(item.getId2())) {
                item.setDefaultSelect(true);
            } else {
                item.setDefaultSelect(false);
            }
        }
        userDepartmentService.saveOrUpdateById1(userDepartments);
    }
}
