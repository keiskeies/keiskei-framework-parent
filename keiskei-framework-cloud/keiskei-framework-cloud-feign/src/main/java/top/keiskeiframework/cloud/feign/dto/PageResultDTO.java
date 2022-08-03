package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 分页结果
 * </p>
 *
 * @param <T> .
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/18 15:45
 */
@Data
public class PageResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 8545996863226528798L;
    protected List<T> data;
    protected Long total;
    protected Long size;
    protected Long offset;
    protected Long page;

    public PageResultDTO() {
        this.data = Collections.emptyList();
        this.total = 0L;
        this.offset = 0L;
        this.size = 10L;
        this.page = 1L;
    }
}
