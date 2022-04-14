package top.keiskeiframework.common.base.dto;

import lombok.Data;
import top.keiskeiframework.common.base.entity.ListEntity;

/**
 * @param <T> .
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
@Data
public class BasePageVO<T extends ListEntity> {

    /**
     * 分页参数
     */
    private Integer page = 1;
    private Integer size = 20;
}
