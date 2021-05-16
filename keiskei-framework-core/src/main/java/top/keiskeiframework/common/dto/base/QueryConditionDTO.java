package top.keiskeiframework.common.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 查询条件
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/14 16:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryConditionDTO {

    /**
     * 字段
     */
    private String column;
    /**
     * 值
     */
    private Object value;
}
