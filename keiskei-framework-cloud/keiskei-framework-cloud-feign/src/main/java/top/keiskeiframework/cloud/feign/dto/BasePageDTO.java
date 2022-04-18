package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @param <ID> .
 * @param <T>  .
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
@Data
public class BasePageDTO<T extends ListEntityDTO> {

    private Integer page = 1;
    private Integer size = 20;


}
