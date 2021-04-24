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

    @Setter
    private Integer page = 1, size = 20;
    @Setter
    private String desc, asc;

    private List<Condition> conditions;
    public void setConditions(String conditions) {
        if (!StringUtils.isEmpty(conditions)) {
            this.conditions = JSON.parseArray(conditions, Condition.class);
        }
    }

    private final static String GE = "ge", GT = "gt", LE = "le", LT = "lt", BT = "bt", LL = "ll", LR = "lr", LIKE = "like", IN = "in";

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

    public Specification<T> getSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

//            // 组装用户部门数据
//            TokenUser tokenUser = SecurityUtils.getSessionUser();
//            if (SystemEnum.SUPER_ADMIN_ID != tokenUser.getId()) {
//                Assert.hasText(tokenUser.getDepartment(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
//                predicates.add(criteriaBuilder.like(root.get("p"), tokenUser.getDepartment() + "%"));
//            }
            if (!CollectionUtils.isEmpty(conditions)) {
                for (Condition baseCondition : conditions) {
                    if (null != baseCondition.getV()) {
                        String column = baseCondition.getC();
                        Object value = baseCondition.getV();
                        String condition = baseCondition.getM();
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
                        switch (condition) {
                            case GT:
                                predicates.add(criteriaBuilder.greaterThan(expression, (Comparable) value));
                                break;
                            case GE:
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value));
                                break;
                            case LT:
                                predicates.add(criteriaBuilder.lessThan(expression, (Comparable) value));
                                break;
                            case LE:
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value));
                                break;
                            case LIKE:
                                predicates.add(criteriaBuilder.like(expression, "%" + value + "%"));
                                break;
                            case LL:
                                predicates.add(criteriaBuilder.like(expression, value + "%"));
                                break;
                            case LR:
                                predicates.add(criteriaBuilder.like(expression, "%" + value));
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
                                        predicates.add(criteriaBuilder.between(expression, (Comparable) value, (Comparable) values[1]));
                                    }
                                }
                                break;
                            default:
                                predicates.add(criteriaBuilder.equal(expression, value));
                                break;
                        }
                    }

                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Data
    @ApiModel(value = "BaseCondition", description = "查询条件")
    public static class Condition {

        @ApiModelProperty(value = "字段名称", dataType = "String")
        @JSONField(name = "condition")
        private String m;

        @ApiModelProperty(value = "字段条件", dataType = "String")
        @JSONField(name = "column")
        private String c;

        @ApiModelProperty(value = "字段值", dataType = "Object")
        @JSONField(name = "value")
        private Object v;
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
