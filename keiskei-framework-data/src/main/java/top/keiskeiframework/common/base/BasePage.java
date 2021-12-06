package top.keiskeiframework.common.base;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class BasePage<T extends ListEntity<ID>, ID extends Serializable> {

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
        return PageRequest.of(this.page - 1, this.size, BaseRequestUtils.getSort(tClass, asc, desc));
    }
}
