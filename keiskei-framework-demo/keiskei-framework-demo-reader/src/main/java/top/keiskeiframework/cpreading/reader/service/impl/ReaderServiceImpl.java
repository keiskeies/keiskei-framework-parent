package top.keiskeiframework.cpreading.reader.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.reader.entity.Reader;
import top.keiskeiframework.cpreading.reader.repository.ReaderRepository;
import top.keiskeiframework.cpreading.reader.service.IReaderService;
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
