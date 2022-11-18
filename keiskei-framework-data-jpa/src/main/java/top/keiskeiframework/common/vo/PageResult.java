package top.keiskeiframework.common.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.IPageResult;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/14 23:58
 */
public class PageResult<T> extends AbstractPageRequest implements IPageResult<T> {

    private static final long serialVersionUID = -4541509938956089562L;

    private Sort sort;

    @Getter
    @Setter
    private long total;
    @Setter
    @Getter
    private long offset;
    @Getter
    @Setter
    private List<T> data;


    public PageResult(BasePageVO basePageVO) {
        super(basePageVO.getPage().intValue(), basePageVO.getSize().intValue());
        this.offset = basePageVO.getOffset();

    }

    protected PageResult(int page, int size, Sort sort) {

        super(page, size);

        Assert.notNull(sort, "Sort must not be null!");

        this.sort = sort;
    }

    public static <T> PageResult<T> of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static <T> PageResult<T> of(int page, int size, Sort sort) {
        return new PageResult<>(page, size, sort);
    }

    public static <T> PageResult<T> of(int page, int size, Sort.Direction direction, String... properties) {
        return of(page, size, Sort.by(direction, properties));
    }

    public static <T> PageResult<T> of(Page<T> page) {
        Pageable pageable = page.getPageable();
        PageResult<T> pageResult = PageResult.of(pageable.getPageNumber(), page.getSize(), pageable.getSort());
        pageResult.setData(page.getContent());
        pageResult.setTotal(page.getTotalElements());
        return pageResult;
    }

    public static <T> PageResult<T> of(List<T> list) {
        int total = CollectionUtils.isEmpty(list) ? 0 : list.size();
        PageResult<T> pageResult = PageResult.of(1, total);
        pageResult.setData(list);
        pageResult.setTotal(total);
        return pageResult;
    }

    @Override
    @NonNull
    public Sort getSort() {
        return sort;
    }

    @Override
    @NonNull
    public Pageable next() {
        return new PageResult<>(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override
    @NonNull
    public PageResult<T> previous() {
        return getPageNumber() == 0 ? this : new PageResult<>(getPageNumber() - 1, getPageSize(), getSort());
    }

    @Override
    @NonNull
    public Pageable first() {
        return new PageResult<>(0, getPageSize(), getSort());
    }

    @Override
    public long getSize() {
        return super.getPageSize();
    }

    @Override
    public long getPage() {
        return super.getPageNumber();
    }

}
