package top.keiskeiframework.common.dto.base;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <p>
 * 解决树形结构总数据量问题
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/12/6 13:13
 */
public class TreePageImpl<T> extends PageImpl<T> {

    private static final long serialVersionUID = -6399242972518644636L;
    private final long total;


    public TreePageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.total = total;
    }

    @Override
    public long getTotalElements() {
        return total;
    }
}
