package top.keiskeiframework.generate.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.generate.entity.FieldEnumAffectInfo;
import top.keiskeiframework.generate.mapper.FieldEnumAffectInfoMapper;
import top.keiskeiframework.generate.service.IFieldEnumAffectInfoService;

/**
 * <p>
 * 表字段枚举 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class FieldEnumAffectInfoServiceImpl extends ListServiceImpl<FieldEnumAffectInfo, Integer, FieldEnumAffectInfoMapper> implements IFieldEnumAffectInfoService {


}
