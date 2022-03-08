package top.keiskeiframework.cpreading.book.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.cpreading.book.entity.BookType;
import top.keiskeiframework.cpreading.book.service.IBookTypeService;
import top.keiskeiframework.cpreading.book.repository.BookTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书类型 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class BookTypeServiceImpl extends TreeServiceImpl<BookType, Long> implements IBookTypeService {

    @Autowired
    private BookTypeRepository bookTypeRepository;

}
