package top.keiskeiframework.common.base;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.base.util.BaseRequestUtils;

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
public class BaseRequest<T extends ListEntity<ID>, ID extends Serializable> {
    /**
     * 默认ID字段
     */
    private static final String ID_COLUMN = "id";
    private static final String PARENT_ID_COLUMN = "parentId";
    /**
     * 展示字段分割符
     */
    private static final String SHOW_SPLIT = ",";

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
     * 查询条件
     */
    @Getter
    private List<QueryConditionDTO> conditions;

    public void setConditions(String conditions) {
        if (!StringUtils.isEmpty(conditions)) {
            this.conditions = JSON.parseArray(conditions, QueryConditionDTO.class);
        }
    }

    @Getter
    private List<String> show;

    public void setShow(String show) {
        if (!StringUtils.isEmpty(show)) {
            this.show = Arrays.stream(show.split(SHOW_SPLIT)).collect(Collectors.toList());
            if (!this.show.contains(ID_COLUMN)) {
                this.show.add(0, ID_COLUMN);
            }

        }
    }

    /**
     * 获取分页条件
     *
     * @return 。
     */
    public Pageable getPageable(@NonNull Class<T> tClass) {
        return PageRequest.of(this.page - 1, this.size, BaseRequestUtils.getSort(tClass, asc, desc));
    }


    @Override
    public String toString() {
        return "BaseRequest{pageNumber=" + page + ", pageSize=" + size + ", desc='" + desc + ", asc='" + asc + ", conditions='" + conditions + '}';
    }
}
