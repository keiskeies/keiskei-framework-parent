package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/8/3 19:16
 */
@Data
public class BaseEntityDTO<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = 7441124718712857171L;
    protected ID id;
}
