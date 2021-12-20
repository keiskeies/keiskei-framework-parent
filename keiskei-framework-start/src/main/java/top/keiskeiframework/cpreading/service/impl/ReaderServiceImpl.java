package top.keiskeiframework.cpreading.service.impl;

import org.springframework.data.domain.ExampleMatcher;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.entity.Reader;
import top.keiskeiframework.cpreading.repository.ReaderRepository;
import top.keiskeiframework.cpreading.service.IReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 读者管理 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class ReaderServiceImpl extends ListServiceImpl<Reader, Long> implements IReaderService {

    @Autowired
    private ReaderRepository readerRepository;
}
