package top.keiskeiframework.common.vo.base;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 排序改变接受类
 * </p>
 *
 * @author cjm
 */
@Data
public class BaseSortDTO implements Serializable {
    private static final long serialVersionUID = -899342361316647661L;

    @NotNull
    private Long id1;
    @NotNull
    private Long sortBy1;
    @NotNull
    private Long id2;
    @NotNull
    private Long sortBy2;
}
