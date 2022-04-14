package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.cloud.feign.enums.ConditionEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
@NoArgsConstructor
@Data
public class BaseRequestDTO<T extends ListEntityDTO> {

    protected Set<String> fields;


    /**
     * 查询条件
     */
    protected List<QueryConditionDTO> conditions;

    /**
     * 组装单一条件
     *
     * @param column 字段
     * @param value  值
     */
    public BaseRequestDTO(String column, Serializable value) {
        this.conditions = Collections.singletonList(
                new QueryConditionDTO(
                        column,
                        ConditionEnum.EQ,
                        Collections.singletonList(value)
                )
        );
    }

    public void addCondition(QueryConditionDTO queryConditionDTO) {
        if (CollectionUtils.isEmpty(this.conditions)) {
            this.conditions = new ArrayList<>();
        }
        this.conditions.add(queryConditionDTO);
    }

    public void addCondition(String column, Serializable value) {
        addCondition(new QueryConditionDTO(column, value));
    }

    /**
     * 显示字段
     */
    protected List<String> show;

    @Override
    public String toString() {
        return "BaseRequest{" +
                "conditions=" + conditions +
                ", show=" + show +
                '}';
    }
}
