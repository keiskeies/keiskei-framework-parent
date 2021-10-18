package top.keiskeiframework.demo.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.demo.entity.Demo;
import top.keiskeiframework.demo.repository.DemoRepository;
import top.keiskeiframework.demo.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 表单测试 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-18 14:23:37
 */
@Service
public class DemoServiceImpl extends ListServiceImpl<Demo, Long> implements IDemoService {

    @Autowired
    private DemoRepository demoRepository;

}
