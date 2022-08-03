package top.keiskeiframework.common.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.IPageResult;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 22:39
 */
@JsonIgnoreProperties(value = {"records"})
public class PageResult<T> extends Page<T> implements IPage<T>, IPageResult<T> {
    private static final long serialVersionUID = 3030882766859160633L;
    private Long offset;

    @Override
    public List<T> getData() {
        return this.records;
    }

    @Override
    public long getPage() {
        return this.current;
    }

    @Override
    public long getOffset() {
        if (null == this.offset) {
            return 0L;
        }
        return this.offset;
    }

    public PageResult(BasePageVO basePage) {
        this.offset = basePage.getOffset();
        this.size = basePage.getSize();
        this.current = basePage.getPage();
    }

    public PageResult(long current, long size, long total) {
        super(current, size, total);
    }

    @Override
    public void setData(List<T> ts) {
        super.setRecords(ts);
    }
}
