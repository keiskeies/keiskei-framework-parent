package top.keiskeiframework.cpreading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.cpreading.entity.BookFeelingLike;
import top.keiskeiframework.cpreading.repository.BookFeelingLikeRepository;
import top.keiskeiframework.cpreading.service.IBookFeelingLikeService;
import top.keiskeiframework.cpreading.service.IBookFeelingService;

/**
 * <p>
 * 图书感悟 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class BookFeelingLikeServiceImpl extends ListServiceImpl<BookFeelingLike, Long> implements IBookFeelingLikeService {

    @Autowired
    private BookFeelingLikeRepository bookFeelingLikeRepository;
    @Autowired
    private IBookFeelingService bookFeelingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookFeelingLike saveAndNotify(BookFeelingLike bookFeelingLike) {
        bookFeelingService.addLike(bookFeelingLike.getBookFeeling().getId());
        return super.saveAndNotify(bookFeelingLike);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdAndNotify(Long id) {
        BookFeelingLike bookFeelingLike = super.findById(id);
        bookFeelingService.delLike(bookFeelingLike.getBookFeeling().getId());
        super.deleteByIdAndNotify(id);
    }
}
