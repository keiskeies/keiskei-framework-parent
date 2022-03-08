package top.keiskeiframework.cpreading.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.entity.Book;
import top.keiskeiframework.cpreading.repository.BookRepository;
import top.keiskeiframework.cpreading.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书管理 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class BookServiceImpl extends ListServiceImpl<Book, Long> implements IBookService {

    @Autowired
    private BookRepository bookRepository;

}
