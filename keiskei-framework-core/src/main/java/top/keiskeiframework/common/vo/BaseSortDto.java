package top.keiskeiframework.common.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 排序改变接受类
 * </p>
 *
 * @author cjm
 */
@Data
public class BaseSortDto {

    @NotNull
    private Long id1;
    @NotNull
    private Long sortBy1;
    @NotNull
    private Long id2;
    @NotNull
    private Long sortBy2;
}
