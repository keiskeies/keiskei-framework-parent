package top.keiskeiframework.cpreading.feeling.service;

import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;
import top.keiskeiframework.cpreading.feeling.entity.ReaderBook;
import top.keiskeiframework.cpreading.feeling.vo.BookTerritoryVO;
import top.keiskeiframework.cpreading.feeling.vo.BookTimelineVO;

import java.util.List;

/**
 * <p>
 * 读者书库 业务接口层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface IReaderBookService extends BaseService<ReaderBook, Long> {

    /**
     * 图书领域
     * <p>
     * //     * @param readerId 读者ID
     *
     * @return 。
     */
    List<BookTerritoryVO> territory();

    /**
     * 领域图书
     *
     * @param typeId 图书类型ID
     * @return 。
     */
    List<Book> territoryDetail(Long typeId);

    List<BookTimelineVO> timeLine(TimeDeltaEnum timeDelta);
}
