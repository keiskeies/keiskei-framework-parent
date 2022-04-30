package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @param <T> .
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/18 15:45
 */
@Data
public class PageResultDTO<T> implements Serializable {
    private static final Long serialVersionUID = 8545996863226528798L;
    protected List<T> records;
    protected Long total;
    protected Long size;
    protected Long current;
    protected ArrayList<OrderItemDTO> orders;

    public PageResultDTO() {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
        this.orders = new ArrayList<>();
    }
}
