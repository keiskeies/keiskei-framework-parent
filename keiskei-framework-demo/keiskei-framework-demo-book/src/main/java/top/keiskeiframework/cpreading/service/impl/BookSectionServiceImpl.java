package top.keiskeiframework.cpreading.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.entity.BookSection;
import top.keiskeiframework.cpreading.repository.BookSectionRepository;
import top.keiskeiframework.cpreading.service.IBookSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书章节 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class BookSectionServiceImpl extends ListServiceImpl<BookSection, Long> implements IBookSectionService {

    @Autowired
    private BookSectionRepository bookSectionRepository;

}
