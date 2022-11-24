package top.keiskeiframework.common.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 分页数据同意返回结果
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 22:18
 */
@Data
@AllArgsConstructor
public class PageResultVO<T> implements Serializable {

    private static final long serialVersionUID = -4543579805704648183L;
    /**
     * 页码
     */
    private Long page;

    /**
     * 分页大小
     */
    private Long size;

    /**
     * offset
     */
    private Long offset;

    /**
     * 总数
     */
    private Long total;

    /**
     * 数据
     */
    private List<T> data;

    public PageResultVO() {
        this.page = 1L;
        this.size = 20L;
        this.offset = 0L;
        this.total = 0L;
        this.data = Collections.emptyList();
    }
}
