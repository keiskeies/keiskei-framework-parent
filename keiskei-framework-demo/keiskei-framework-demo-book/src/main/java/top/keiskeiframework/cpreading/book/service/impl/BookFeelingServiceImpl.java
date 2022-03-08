package top.keiskeiframework.cpreading.book.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.cpreading.book.entity.BookFeeling;
import top.keiskeiframework.cpreading.book.service.IBookFeelingService;
import top.keiskeiframework.cpreading.book.repository.BookFeelingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书感悟 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class BookFeelingServiceImpl extends TreeServiceImpl<BookFeeling, Long> implements IBookFeelingService {

    @Autowired
    private BookFeelingRepository bookFeelingRepository;

    @Override
    public int addLike(Long id) {
        return bookFeelingRepository.addLikeNum(id);
    }

    @Override
    public int delLike(Long id) {
        return bookFeelingRepository.delLikeNum(id);
    }

}
