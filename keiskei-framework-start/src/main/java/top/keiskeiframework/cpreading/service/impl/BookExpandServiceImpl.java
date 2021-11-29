package top.keiskeiframework.cpreading.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.cpreading.entity.BookExpand;
import top.keiskeiframework.cpreading.repository.BookExpandRepository;
import top.keiskeiframework.cpreading.service.IBookExpandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书拓展 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class BookExpandServiceImpl extends TreeServiceImpl<BookExpand, Long> implements IBookExpandService {

    @Autowired
    private BookExpandRepository bookExpandRepository;

}
