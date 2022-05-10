package top.keiskeiframework.common.base.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 排序改变接受类
 * </p>
 * @param <ID> .
 * @author cjm
 */
@Data
public class BaseSortVO<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = -899342361316647661L;

    /**
     * id1
     */
    @NotNull
    private ID id1;

    /**
     * sortby1 修改后的排序
     */
    @NotNull
    private Integer sortBy1;
    /**
     * ID2
     */
    @NotNull
    private ID id2;
    /**
     * 修改后的排序
     */
    @NotNull
    private Integer sortBy2;
}
