package top.keiskeiframework.common.base;

import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 接口
 * </p>
 *
 * @author James Chen
 * @since 2022/11/25 11:26
 */
public interface QueryCondition {

    QueryCondition addCondition(String condition);

    QueryCondition addCondition(String c, Serializable v);

    QueryCondition addCondition(String c, ConditionEnum d, Serializable v);

    QueryCondition addCondition(String c, Serializable... vs);

    QueryCondition addCondition(String c, List<Serializable> vs);

    QueryCondition addCondition(String c, ConditionEnum d, List<Serializable> vs);

    QueryCondition addCondition(QueryConditionVO condition);

    QueryCondition addConditions(List<QueryConditionVO> conditions);
}
