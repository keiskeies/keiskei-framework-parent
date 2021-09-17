package top.keiskeiframework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.util.BaseRequestUtils;

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
@Data
@ApiModel(value = "BaseRequest", description = "查询条件")
public class BaseRequest<T extends BaseEntity> {
    /**
     * 默认排序字段
     */
    private static final String DEFAULT_ORDER_COLUMN = "id";

    @ApiModelProperty(value = "页码", dataType = "Integer", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页条数", dataType = "Integer", example = "20")
    private Integer size = 20;

    @ApiModelProperty(value = "降序字段", dataType = "String", example = "id")
    private String desc;

    @ApiModelProperty(value = "升序字段", dataType = "String", example = "id")
    private String asc;

    @ApiModelProperty(value = "查询字段", dataType = "String", example = "id,name")
    private String show;

    @ApiModelProperty(value = "查询条件", dataType = "String", example = "{\"column\":\"id\", \"condition\": \"EQ|IN|GE|GT|LIKE|LL|LR|BT\", \"value\": [1001,1002] }")
    private String conditions;

    /**
     * 获取分页条件
     *
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

        if (!CollectionUtils.isEmpty(orders)) {
            Sort sort = Sort.by(orders);
            return PageRequest.of(this.page - 1, this.size, sort);
        } else {
            return PageRequest.of(this.page - 1, this.size);
        }


    }

    /**
     * 获取查询关系
     *
     * @return 。
     */
    public Query getQuery(@NonNull Class<T> tClass) {
        return BaseRequestUtils.getQuery(
                JSON.parseArray(conditions, QueryConditionDTO.class),
                StringUtils.isEmpty(show) ? null : show.split(","),
                tClass);
    }


    @Override
    public String toString() {
        return "BaseRequest{pageNumber=" + page + ", pageSize=" + size + ", desc='" + desc + ", asc='" + asc + ", conditions='" + conditions + '}';
    }
}
