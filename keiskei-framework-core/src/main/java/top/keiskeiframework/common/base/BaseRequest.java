package top.keiskeiframework.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.util.BaseRequestUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
public class BaseRequest<T> {

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

        Sort sort = Sort.by(orders);

        return PageRequest.of(this.page - 1, this.size, sort);
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
        return "BaseRequest{" +
                "pageNumber=" + page +
                ", pageSize=" + size +
                ", desc='" + desc +
                ", asc='" + asc +
                ", conditions='" + conditions +
                '}';
    }
}
