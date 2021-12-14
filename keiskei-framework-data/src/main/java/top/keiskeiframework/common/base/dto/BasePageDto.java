package top.keiskeiframework.common.base.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.util.BaseRequestUtils;

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
public class BasePageDto<T extends ListEntity<ID>, ID extends Serializable> {

    /**
     * 分页参数
     */
    @Setter
    @Getter
    private Integer page = 1, size = 20;
    /**
     * 排序方式
     */
    @Setter
    @Getter
    private String desc, asc;

    /**
     * 获取分页条件
     *
     * @return 。
     */
    public Pageable getPageable(@NonNull Class<T> tClass) {
        return PageRequest.of(
                this.page - 1, this.size,
                BaseRequestUtils.getSort(tClass, asc, desc));
    }
}
