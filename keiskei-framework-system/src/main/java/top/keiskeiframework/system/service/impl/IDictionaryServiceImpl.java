package top.keiskeiframework.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.system.entity.Dictionary;
import top.keiskeiframework.system.repository.DictionaryRepository;
import top.keiskeiframework.system.service.IDictionaryService;

/**
 * <p>
 * 数据字典 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Service
public class IDictionaryServiceImpl extends TreeServiceImpl<Dictionary, Long> implements IDictionaryService {

    @Autowired
    private DictionaryRepository dictionaryRepository;


}
