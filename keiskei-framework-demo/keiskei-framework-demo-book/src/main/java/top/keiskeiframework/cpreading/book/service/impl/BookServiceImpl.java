package top.keiskeiframework.cpreading.book.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.book.entity.Book;
import top.keiskeiframework.cpreading.book.repository.BookRepository;
import top.keiskeiframework.cpreading.book.service.IBookService;
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
