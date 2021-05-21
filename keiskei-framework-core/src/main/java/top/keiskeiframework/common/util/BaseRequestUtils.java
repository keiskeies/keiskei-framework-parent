package top.keiskeiframework.common.util;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 请求处理工具
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/20 18:42
 */
public class BaseRequestUtils {
    /**
     * 获取Specification
     *
     * @param conditions 查询条件
     * @param <T>        实体类
     * @return 。
     */
    public static <T> Specification<T> getSpecification(List<QueryConditionDTO> conditions) {
        return (root, query, builder) -> builder.and(getPredicates(conditions, builder, root).toArray(new Predicate[0]));
    }

    /**
     * 拼装query
     *
     * @param conditions 。
     * @param builder    。
     * @param tClass     。
     * @param <T>        。
     * @return 。
     */
    public static <T> CriteriaQuery<T> getCriteriaQuery(List<QueryConditionDTO> conditions, CriteriaBuilder builder, Class<T> tClass) {
        CriteriaQuery<T> query = builder.createQuery(tClass);
        return query.where(getPredicates(conditions, builder, query.from(tClass)).toArray(new Predicate[0]));
    }

    private static <T> List<Predicate> getPredicates(List<QueryConditionDTO> conditions, CriteriaBuilder builder, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();

//            // 组装用户部门数据
//            TokenUser tokenUser = SecurityUtils.getSessionUser();
//            if (SystemEnum.SUPER_ADMIN_ID != tokenUser.getId()) {
//                Assert.hasText(tokenUser.getDepartment(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
//                predicates.add(builder.like(root.get("p"), tokenUser.getDepartment() + "%"));
//            }
        if (!CollectionUtils.isEmpty(conditions)) {
            for (QueryConditionDTO baseCondition : conditions) {
                if (null != baseCondition.getV()) {
                    String column = baseCondition.getC();
                    Object value = baseCondition.getV();
                    // 需要限定的参数
                    Expression expression;
                    // 若为关联表 , 则使用左连接
                    if (column.contains(".")) {
                        String[] columns = column.split("\\.");
                        Join join = root.join(columns[0], JoinType.LEFT);
                        expression = join.get(columns[1]);
                    } else {
                        expression = root.get(column);
                    }
                    switch (baseCondition.getM()) {
                        case GT:
                            predicates.add(builder.greaterThan(expression, (Comparable) value));
                            break;
                        case GE:
                            predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) value));
                            break;
                        case LT:
                            predicates.add(builder.lessThan(expression, (Comparable) value));
                            break;
                        case LE:
                            predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) value));
                            break;
                        case LIKE:
                            predicates.add(builder.like(expression, "%" + value + "%"));
                            break;
                        case LL:
                            predicates.add(builder.like(expression, value + "%"));
                            break;
                        case LR:
                            predicates.add(builder.like(expression, "%" + value));
                            break;
                        case IN:
                            if (value instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) value;
                                predicates.add(expression.in(jsonArray.toArray()));
                            }
                            break;
                        case BT:
                            if (value instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) value;
                                Object[] values = jsonArray.toArray();
                                if (values.length == 2) {
                                    predicates.add(builder.between(expression, (Comparable) value, (Comparable) values[1]));
                                }
                            }
                            break;
                        default:
                            predicates.add(builder.equal(expression, value));
                            break;
                    }
                }

            }
        }
        return predicates;
    }
}
