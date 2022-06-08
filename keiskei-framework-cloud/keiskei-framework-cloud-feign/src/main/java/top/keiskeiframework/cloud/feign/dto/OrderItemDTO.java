package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 降序字段
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/18 15:48
 */
@Data
public class OrderItemDTO implements Serializable {

    private static final long serialVersionUID = 8099586250668537045L;

    private String column;
    private Boolean asc;

    public OrderItemDTO() {
        this.asc = true;
    }
}