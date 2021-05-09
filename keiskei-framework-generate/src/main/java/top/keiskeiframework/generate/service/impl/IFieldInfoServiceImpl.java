package top.keiskeiframework.generate.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.generate.entity.FieldInfo;
import top.keiskeiframework.generate.repository.FieldInfoRepository;
import top.keiskeiframework.generate.service.IFieldInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 表字段信息 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class IFieldInfoServiceImpl extends ListServiceImpl<FieldInfo> implements IFieldInfoService {

    @Autowired
    private FieldInfoRepository fieldInfoRepository;

}