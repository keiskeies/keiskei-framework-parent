package top.keiskeiframework.common.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 请求数基础接口
 * </p>
 *
 * @author James Chen
 * @since 2022/11/24 17:14
 */
@Data
public class BaseRequestVO implements Serializable {
    private static final long serialVersionUID = -812828147724344918L;
    private String desc;
    private String asc;
    private Boolean complete;
    private Boolean tree;
    private String show;
    private String conditions;

    public BaseRequestVO() {
        this.complete = false;
        this.tree = true;
    }


}
