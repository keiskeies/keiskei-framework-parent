package top.keiskeiframework.common.base;

import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.entity.SuperEntity;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.util.BaseRequestUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
public class BaseRequest<T extends SuperEntity<ID>, ID extends Serializable> {
    /**
     * 默认排序字段
     */
    private static final String DEFAULT_ORDER_COLUMN = "createTime";

    /**
     * 分页参数
     */
    @Setter
    private Integer page = 1, size = 20;
    /**
     * 排序方式
     */
    @Setter
    private String desc, asc;

    /**
     * 查询条件
     */
    private List<QueryConditionDTO> conditions;
    public void setConditions(String conditions) {
        if (!StringUtils.isEmpty(conditions)) {
            this.conditions = JSON.parseArray(conditions, QueryConditionDTO.class);
        }
    }

    /**
     * 获取分页条件
     * @return 。
     */
    public Pageable getPageable() {
        List<Sort.Order> orders = new ArrayList<>();
        if (!StringUtils.isEmpty(desc)) {
            orders.add(Sort.Order.desc(desc));
        }
        if (!StringUtils.isEmpty(asc)) {
            orders.add(Sort.Order.asc(asc));
        }

        if (CollectionUtils.isEmpty(orders)) {
            orders.add(Sort.Order.desc(DEFAULT_ORDER_COLUMN));
        }

        Sort sort = Sort.by(orders);

        return PageRequest.of(this.page - 1, this.size, sort);
    }
    /**
     * 获取分页条件
     * @return 。
     */
    public Pageable getPageable(@NonNull Class<T> tClass) {
        Set<String> fieldSet = BaseRequestUtils.getFieldSet(tClass);
        List<Sort.Order> orders = new ArrayList<>();
        if (!StringUtils.isEmpty(desc) && fieldSet.contains(desc)) {
            orders.add(Sort.Order.desc(desc));
        }
        if (!StringUtils.isEmpty(asc) && fieldSet.contains(desc)) {
            orders.add(Sort.Order.asc(asc));
        }

        if (CollectionUtils.isEmpty(orders)) {
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                SortBy sortBy = field.getAnnotation(SortBy.class);
                if (null != sortBy) {
                    orders.add(new Sort.Order(sortBy.desc() ? Sort.Direction.DESC : Sort.Direction.ASC, field.getName()));
                }
            }
            if (CollectionUtils.isEmpty(orders)) {
                orders.add(Sort.Order.desc(DEFAULT_ORDER_COLUMN));
            }
        }

        Sort sort = Sort.by(orders);

        return PageRequest.of(this.page - 1, this.size, sort);
    }

    /**
     * 获取查询关系
     * @return 。
     */
    public Specification<T> getSpecification(@NonNull Class<T> tClass) {
        return BaseRequestUtils.getSpecification(conditions, tClass);
    }


    /**
     * 获取查询关系
     * @return 。
     */
    public Specification<T> getSpecification() {
        return BaseRequestUtils.getSpecification(conditions);
    }


    @Override
    public String toString() {
        return "BaseRequest{pageNumber=" + page + ", pageSize=" + size + ", desc='" + desc + ", asc='" + asc + ", conditions='" + conditions + '}';
    }
}
