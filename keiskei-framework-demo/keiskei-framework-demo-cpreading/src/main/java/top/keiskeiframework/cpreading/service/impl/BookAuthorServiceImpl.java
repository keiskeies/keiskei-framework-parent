package top.keiskeiframework.cpreading.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.entity.BookAuthor;
import top.keiskeiframework.cpreading.repository.BookAuthorRepository;
import top.keiskeiframework.cpreading.service.IBookAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书作者 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class BookAuthorServiceImpl extends ListServiceImpl<BookAuthor, Long> implements IBookAuthorService {

    @Autowired
    private BookAuthorRepository bookAuthorRepository;

}
