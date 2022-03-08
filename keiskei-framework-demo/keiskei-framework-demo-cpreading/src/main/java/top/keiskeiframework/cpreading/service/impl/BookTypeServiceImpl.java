package top.keiskeiframework.cpreading.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.cpreading.entity.BookType;
import top.keiskeiframework.cpreading.repository.BookTypeRepository;
import top.keiskeiframework.cpreading.service.IBookTypeService;
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
