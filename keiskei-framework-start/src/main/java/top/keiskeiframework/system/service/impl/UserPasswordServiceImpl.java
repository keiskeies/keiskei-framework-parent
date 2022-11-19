package top.keiskeiframework.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.base.mp.service.impl.MpBaseServiceImpl;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.system.dto.UserPasswordDTO;
import top.keiskeiframework.system.entity.UserPassword;
import top.keiskeiframework.system.mapper.UserPasswordMapper;
import top.keiskeiframework.system.properties.SystemProperties;
import top.keiskeiframework.system.service.IUserPasswordService;
import top.keiskeiframework.system.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户密码
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/15 15:54
 */
@Service
public class UserPasswordServiceImpl extends MpBaseServiceImpl<UserPassword, Integer, UserPasswordMapper> implements IUserPasswordService {


    @Autowired
    private SystemProperties systemProperties;

    @Override
    public void initPassword(Integer userId) {
        UserPassword userPassword = new UserPassword();
        userPassword.setUserId(userId);
        userPassword.setPassword(SecurityUtils.encodePassword(systemProperties.getDefaultPassword()));
        userPassword.setFirstSet(Boolean.TRUE);
        super.saveOne(userPassword);
    }

    @Override
    public void updatePassword(Integer userId, UserPasswordDTO userPassword) {

        // 查询最近修改的密码
        LocalDateTime lastTime = LocalDateTime.now().plusDays(-1L * systemProperties.getPasswordRepeatMinDay());

        BaseRequestVO<UserPassword, Integer> request = new BaseRequestVO<>();
        request.addCondition(new QueryConditionVO("userId", userId));
        request.addCondition(new QueryConditionVO("createTime", ConditionEnum.GT, lastTime));

        List<UserPassword> userPasswordRecords = super.findListByCondition(request);


        // 校验最近密码重复
        if (!CollectionUtils.isEmpty(userPasswordRecords)) {

            for (int i = 0; i < userPasswordRecords.size(); i++) {
                UserPassword record = userPasswordRecords.get(i);
                if (i == 0) {
                    if (!userPassword.matchOld(record.getPassword())) {
                        throw new BizException(BizExceptionEnum.AUTH_PASSWORD_ERROR);
                    }
                }

                if (userPassword.matchNew(record.getPassword())) {
                    throw new BizException(BizExceptionEnum.AUTH_PASSWORD_ERROR.getCode(),
                            "密码与" + systemProperties.getPasswordRepeatMinDay() + "天内修改记录重复");
                }
            }
        }

        UserPassword item = new UserPassword();
        item.setUserId(userId);
        item.setPassword(userPassword.getNewPassword());

        super.saveOne(item);
    }
}
