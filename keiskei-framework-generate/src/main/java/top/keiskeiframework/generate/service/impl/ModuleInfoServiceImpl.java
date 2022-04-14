package top.keiskeiframework.generate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.generate.entity.ModuleInfo;
import top.keiskeiframework.generate.mapper.ModuleInfoMapper;
import top.keiskeiframework.generate.service.IModuleInfoService;

/**
 * <p>
 * 模块信息 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
@Slf4j
public class ModuleInfoServiceImpl extends ListServiceImpl<ModuleInfo> implements IModuleInfoService {

    @Autowired
    private ModuleInfoMapper moduleInfoMapper;

}
