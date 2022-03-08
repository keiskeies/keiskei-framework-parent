package top.keiskeiframework.cpreading.feeling.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.feeling.entity.BookExpand;
import top.keiskeiframework.cpreading.feeling.repository.BookExpandRepository;
import top.keiskeiframework.cpreading.feeling.service.IBookExpandService;
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
public class BookExpandServiceImpl extends ListServiceImpl<BookExpand, Long> implements IBookExpandService {

    @Autowired
    private BookExpandRepository bookExpandRepository;

}
