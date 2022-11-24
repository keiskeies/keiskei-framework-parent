package top.keiskeiframework.common.base.jpa.vo;

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
import top.keiskeiframework.common.base.dto.PageResultVO;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/14 23:58
 */
public class JpaPageRequest extends AbstractPageRequest implements Pageable {

    private static final long serialVersionUID = -4541509938956089562L;
    private Sort sort;
    @Getter
    @Setter
    private long total;
    @Setter
    @Getter
    private long offset;


    public JpaPageRequest(BasePageVO BasePageVO) {
        super(BasePageVO.getPage().intValue(), BasePageVO.getSize().intValue());
        this.offset = BasePageVO.getOffset();

    }

    protected JpaPageRequest(int page, int size, Sort sort) {
        super(page, size);
        Assert.notNull(sort, "Sort must not be null!");
        this.sort = sort;
    }

    @Override
    @NonNull
    public Sort getSort() {
        return sort;
    }

    @Override
    @NonNull
    public Pageable next() {
        return new JpaPageRequest(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override
    @NonNull
    public JpaPageRequest previous() {
        return getPageNumber() == 0 ? this : new JpaPageRequest(getPageNumber() - 1, getPageSize(), getSort());
    }

    @Override
    @NonNull
    public Pageable first() {
        return new JpaPageRequest(0, getPageSize(), getSort());
    }

}
