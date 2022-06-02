package top.keiskeiframework.common.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
@Data
public class BasePageVO implements Serializable {

    private static final long serialVersionUID = -100073037476383441L;
    /**
     * 分页参数
     */
    private Long page;
    private Long offset;
    private Long size;

    public BasePageVO() {
        this.page = 1L;
        this.size = 20L;
    }
}
