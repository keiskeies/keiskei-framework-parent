package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.keiskeiframework.cloud.feign.enums.ConditionEnum;

import java.io.Serializable;
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
public class BaseRequestDTO<T extends ListEntityDTO<ID>, ID extends Serializable> {

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
