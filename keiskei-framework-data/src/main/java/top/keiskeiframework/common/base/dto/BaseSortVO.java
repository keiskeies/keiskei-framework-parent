package top.keiskeiframework.common.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 排序
 * </p>
 *
 * @param <ID> .
 * @author cjm
 */
@Data
public class BaseSortVO<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = -899342361316647661L;

    /**
     * 排序方式
     */
    private String desc;
    private String asc;
}
