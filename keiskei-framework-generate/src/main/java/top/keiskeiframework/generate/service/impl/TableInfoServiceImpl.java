package top.keiskeiframework.generate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.generate.entity.TableInfo;
import top.keiskeiframework.generate.repository.TableInfoMapper;
import top.keiskeiframework.generate.service.ITableInfoService;

/**
 * <p>
 * 表结构信息 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class TableInfoServiceImpl extends ListServiceImpl<TableInfo, Long> implements ITableInfoService {

    @Autowired
    private TableInfoMapper tableInfoMapper;

}
