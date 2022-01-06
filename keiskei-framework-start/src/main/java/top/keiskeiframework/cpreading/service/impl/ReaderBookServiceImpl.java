package top.keiskeiframework.cpreading.service.impl;

import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;
import top.keiskeiframework.common.util.MdcUtils;
import top.keiskeiframework.cpreading.entity.Book;
import top.keiskeiframework.cpreading.entity.BookType;
import top.keiskeiframework.cpreading.entity.Reader;
import top.keiskeiframework.cpreading.entity.ReaderBook;
import top.keiskeiframework.cpreading.repository.ReaderBookRepository;
import top.keiskeiframework.cpreading.service.IReaderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.keiskeiframework.cpreading.vo.BookTerritoryVO;
import top.keiskeiframework.cpreading.vo.BookTimelineVO;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 读者书库 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@Service
public class ReaderBookServiceImpl extends ListServiceImpl<ReaderBook, Long> implements IReaderBookService {

    @Autowired
    private ReaderBookRepository readerBookRepository;

    @Override
    public List<BookTerritoryVO> territory() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<ReaderBook> root = query.from(ReaderBook.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<ReaderBook, Reader> readerJoin = root.join("reader", JoinType.INNER);
        Expression<Long> readerExpression = readerJoin.get("id");
        predicates.add(criteriaBuilder.equal(readerExpression, MdcUtils.getLongUserId()));
        query.where(predicates.toArray(new Predicate[0]));


        Join<ReaderBook, Book> bookJoin = root.join("book", JoinType.INNER);
        Expression<BookType> bookTypeExpression = bookJoin.get("type");
        query.groupBy(bookTypeExpression);


        query.multiselect(
                bookTypeExpression,
                criteriaBuilder.count(root).alias("number")

        );


        List<Tuple> tuples = entityManager.createQuery(query).getResultList();
        if (!CollectionUtils.isEmpty(tuples)) {
            List<BookTerritoryVO> result = new ArrayList<>(tuples.size());
            for (Tuple tuple : tuples) {
                result.add(new BookTerritoryVO(tuple.get(0, BookType.class), tuple.get(1, Long.class).intValue()));
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> territoryDetail(Long typeId) {




        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReaderBook> query = criteriaBuilder.createQuery(ReaderBook.class);
        Root<ReaderBook> root = query.from(ReaderBook.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<ReaderBook, Reader> readerJoin = root.join("reader", JoinType.INNER);
        Expression<Long> readerExpression = readerJoin.get("id");
        predicates.add(criteriaBuilder.equal(readerExpression, MdcUtils.getLongUserId()));



        Join<ReaderBook, Book> bookJoin = root.join("book", JoinType.INNER);
        Join<Book, BookType> bookTypeJoin = bookJoin.join("type", JoinType.INNER);
        Expression<Long> bookTypeId = bookTypeJoin.get("id");
        predicates.add(criteriaBuilder.equal(bookTypeId, typeId));

        query.where(predicates.toArray(new Predicate[0]));
        List<ReaderBook> readerBooks = entityManager.createQuery(query).getResultList();
        if (!CollectionUtils.isEmpty(readerBooks)) {
            return readerBooks.stream().map(ReaderBook::getBook).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BookTimelineVO> timeLine(TimeDeltaEnum timeDelta) {
        ChartRequestDTO chartRequestDTO = new ChartRequestDTO();
        chartRequestDTO.setColumn("createTime");
        chartRequestDTO.setColumnType(ColumnType.TIME);
        chartRequestDTO.setTimeDelta(timeDelta);

        Map<String, List<String>> conditions = new HashMap<>(1);
        conditions.put("createUserId", Collections.singletonList(MdcUtils.getUserId()));

        chartRequestDTO.setConditions(conditions);

        Map<String, Double> data = getChartOptions(chartRequestDTO);
        if (!CollectionUtils.isEmpty(data)) {
            List<BookTimelineVO> result = new ArrayList<>();
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                result.add(new BookTimelineVO(entry.getKey(), entry.getValue().intValue()));
            }
            result = result.stream().sorted(Comparator.comparing(BookTimelineVO::getDate).reversed()).collect(Collectors.toList());
            return result;
        } else {
            return null;
        }
    }
}
