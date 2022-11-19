package top.keiskeiframework.system.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.system.entity.UserLoginRecord;
import top.keiskeiframework.system.mapper.UserLoginRecordMapper;
import top.keiskeiframework.system.service.IUserLoginRecordService;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 21:24
 */
@Service
public class UserLoginRecordServiceImpl extends MpListServiceImpl<UserLoginRecord, Long, UserLoginRecordMapper> implements IUserLoginRecordService {
}
