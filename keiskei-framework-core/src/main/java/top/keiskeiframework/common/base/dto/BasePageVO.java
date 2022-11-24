package top.keiskeiframework.common.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 分页参数接口
 * </p>
 *
 * @author James Chen
 * @since 2022/11/24 18:20
 */
@Data
public class BasePageVO implements Serializable {
    private static final long serialVersionUID = -8279619362417097492L;
    private Long page;
    private Long offset;
    private Long size;
    private Boolean all;

    public BasePageVO() {
        this.page = 1L;
        this.size = 20L;
        this.all = false;
    }

}
