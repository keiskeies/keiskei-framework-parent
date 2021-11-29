package top.keiskeiframework.cpreading.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.entity.ReaderBook;
import top.keiskeiframework.cpreading.repository.ReaderBookRepository;
import top.keiskeiframework.cpreading.service.IReaderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 读者书库 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class ReaderBookServiceImpl extends ListServiceImpl<ReaderBook, Long> implements IReaderBookService {

    @Autowired
    private ReaderBookRepository readerBookRepository;

}
