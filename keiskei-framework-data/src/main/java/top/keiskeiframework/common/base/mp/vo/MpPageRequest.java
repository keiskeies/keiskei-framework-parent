package top.keiskeiframework.common.base.mp.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import top.keiskeiframework.common.base.dto.BasePageVO;

/**
 * <p>
 * mp page
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 22:39
 */
public class MpPageRequest<T> extends Page<T> implements IPage<T> {
    private static final long serialVersionUID = 3030882766859160633L;

    @Getter
    private final Long offset;

    @Override
    public long offset() {
        if (null == offset) {
            return super.offset();
        } else {
            return offset;
        }
    }

    public MpPageRequest(BasePageVO basePage) {
        this.size = basePage.getSize();
        this.current = basePage.getPage();
        this.offset = basePage.getOffset();
    }
}
