package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.system.entity.UserSetting;
import top.keiskeiframework.system.mapper.UserSettingMapper;
import top.keiskeiframework.system.service.IUserSettingService;

/**
 * <p>
 * 用户设置
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/18 15:08
 */
@Service
public class UserSettingServiceImpl extends MpListServiceImpl<UserSetting, Integer, UserSettingMapper> implements IUserSettingService {
}
