package top.keiskeiframework.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.keiskeiframework.common.base.dto.BasePageVO;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/5/10 23:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PageDTO<T> extends Page<T> implements IPage<T> {

    private Long offset;


    @Override
    public long offset() {
        if (null != offset) {
            return offset;
        }
        return super.offset();
    }

    public PageDTO(BasePageVO basePage) {
        this.offset = basePage.getOffset();
        this.size = basePage.getSize();
        this.current = basePage.getPage();
    }

}
