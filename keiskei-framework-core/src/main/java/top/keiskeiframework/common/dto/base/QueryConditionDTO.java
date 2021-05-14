package top.keiskeiframework.common.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/14 16:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryConditionDTO {

    private String column;
    private Object value;
}
