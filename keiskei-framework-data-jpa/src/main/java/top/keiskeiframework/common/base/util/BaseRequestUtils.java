package top.keiskeiframework.common.base.util;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.annotation.SortBy;
import top.keiskeiframework.common.base.constants.BaseConstants;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.base.enums.SystemEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.MdcUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 请求处理工具
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author v_chenjiamin
 * @since 2021/5/20 18:42
 */
@Component
public class BaseRequestUtils<T extends IBaseEntity<ID>, ID extends Serializable> {
    private final static String JOIN_SPLIT = ".";
    private final static String JOIN_SPLIT_REGEX = "\\.";
    private final static String CONDITION_SPLIT_REGEX = ",";
    private final static String LIKE_FIX = "%";
    private final static String DEPARTMENT_COLUMN = "p";
    private final static String PARENT_COLUMN = "parentId";

    @Value("${keiskei.use-department:false}")
    public void setUseDepartment(Boolean useDepartment) {
        BaseRequestUtils.useDepartment = useDepartment;
    }

    /**
     * 是否启用部门权限
     */
    private static boolean useDepartment = false;

    /**
     * 获取查询的指定字段
     *
     * @param root    root
     * @param show    show
     * @param <T>     T
     * @param <ID>    ID
     * @param joinMap joinMap
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> List<Selection<?>> getSelections(Root<T> root, List<String> show, Map<String, Join<T, ?>> joinMap) {
        List<Selection<?>> selections = new ArrayList<>(show.size());

        for (String showColumn : show) {
            if (showColumn.contains(JOIN_SPLIT)) {
                String[] columns = showColumn.split(JOIN_SPLIT_REGEX);
                Join<T, ?> join = getJoin(root, columns, joinMap);
                selections.add(join.get(columns[1]));
            } else {
                selections.add(root.get(showColumn));
            }
        }
        return selections;
    }

    /**
     * 获取排序条件
     *
     * @param root   root
     * @param tClass tClass
     * @param <T>    T
     * @param <ID>   ID
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> List<Order> getOrders(Root<T> root, Class<T> tClass) {
        return getOrders(root, tClass, null);
    }

    /**
     * 获取排序
     *
     * @param root    root
     * @param tClass  tClass
     * @param asc     asc
     * @param desc    desc
     * @param <T>     T
     * @param <ID>    ID
     * @param joinMap joinMap
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> List<Order> getOrders(Root<T> root, Class<T> tClass, String asc, String desc, Map<String, Join<T, ?>> joinMap) {
        List<Order> orders = new ArrayList<>();
        if (tClass.getSuperclass().equals(TreeEntity.class)) {
            orders.add(new OrderImpl(root.get(PARENT_COLUMN), true));
        }
        if (!StringUtils.isEmpty(desc)) {
            for (String sc : desc.split(CONDITION_SPLIT_REGEX)) {
                orders.add(getOrderImpl(root, sc, false, joinMap));
            }
        }
        if (!StringUtils.isEmpty(asc)) {
            for (String sc : asc.split(CONDITION_SPLIT_REGEX)) {
                orders.add(getOrderImpl(root, sc, true, joinMap));
            }
        }
        return getOrders(root, tClass, orders);
    }


    /**
     * 获取排序条件
     *
     * @param root   root
     * @param tClass tClass
     * @param orders orders
     * @param <T>    T
     * @param <ID>   ID
     * @return .
     */
    private static <T extends IBaseEntity<ID>, ID extends Serializable> List<Order> getOrders(Root<T> root, Class<T> tClass, List<Order> orders) {
        if (null == orders) {
            orders = new ArrayList<>();
        }
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                orders.add(new OrderImpl(root.get(field.getName()), !sortBy.desc()));
            }
        }

        if (CollectionUtils.isEmpty(orders)) {
            orders.add(new OrderImpl(root.get(BaseConstants.DEFAULT_ORDER_COLUMN), false));
        }
        return orders;
    }


    /**
     * 获取排序
     *
     * @param root    root
     * @param sc      sc
     * @param asc     asc
     * @param joinMap joinMap
     * @param <T>     T
     * @param <ID>    ID
     * @return .
     */
    private static <T extends IBaseEntity<ID>, ID extends Serializable> OrderImpl getOrderImpl(Root<T> root, String sc, boolean asc, Map<String, Join<T, ?>> joinMap) {
        if (sc.contains(JOIN_SPLIT)) {
            String[] columns = sc.split(JOIN_SPLIT_REGEX);
            Join<T, ?> join = getJoin(root, columns, joinMap);
            return new OrderImpl(join.get(columns[1]), asc);
        } else {
            return new OrderImpl(root.get(sc), asc);
        }
    }

    /**
     * 获取中间JOIN
     *
     * @param root    root
     * @param columns column
     * @param joinMap joinMap
     * @param <T>     T
     * @param <ID>    ID
     * @return .
     */
    private static <T extends IBaseEntity<ID>, ID extends Serializable> Join<T, ?> getJoin(Root<T> root, String[] columns, Map<String, Join<T, ?>> joinMap) {
        Join<T, ?> join = joinMap.get(columns[0]);
        if (null == join) {
            join = root.join(columns[0], JoinType.LEFT);
            joinMap.put(columns[0], join);
        }
        return join;
    }


    /**
     * 获取Pageable的排序条件
     *
     * @param tClass tClass
     * @param asc    asc
     * @param desc   desc
     * @param <T>    T
     * @param <ID>   ID
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> Sort getSort(Class<T> tClass, String asc, String desc) {
        List<Sort.Order> orders = new ArrayList<>();

        if (tClass.getSuperclass().equals(TreeEntity.class)) {
            orders.add(Sort.Order.asc(PARENT_COLUMN));
        }

        if (!StringUtils.isEmpty(desc)) {
            orders.add(Sort.Order.desc(desc));
        }

        if (!StringUtils.isEmpty(asc)) {
            orders.add(Sort.Order.asc(asc));
        }
        return getSort(tClass, orders);
    }

    /**
     * 获取Pageable的排序条件
     *
     * @param tClass tClass
     * @param orders orders
     * @param <T>    T
     * @param <ID>   ID
     * @return .
     */
    private static <T extends IBaseEntity<ID>, ID extends Serializable> Sort getSort(Class<T> tClass, List<Sort.Order> orders) {
        if (null == orders) {
            orders = new ArrayList<>();
        }
        for (Field field : tClass.getDeclaredFields()) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                if (sortBy.desc()) {
                    orders.add(Sort.Order.desc(field.getName()));
                } else {
                    orders.add(Sort.Order.asc(field.getName()));
                }
            }
        }
        if (CollectionUtils.isEmpty(orders)) {
            orders.add(Sort.Order.desc(BaseConstants.DEFAULT_ORDER_COLUMN));
        }
        return Sort.by(orders);
    }

    /**
     * 获取Pageable的排序条件
     *
     * @param tClass tClass
     * @param <T>    T
     * @param <ID>   ID
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> Sort getSort(Class<T> tClass) {
        return getSort(tClass, null);
    }

    /**
     * 查询数据
     *
     * @param query  query
     * @param show   show
     * @param tClass tClass
     * @param <T>    t
     * @param <ID>   id
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> List<T> queryDataList(CriteriaQuery<Tuple> query, List<String> show, Class<T> tClass, EntityManager entityManager) {
        return queryDataList(query, null, null, show, tClass, entityManager);
    }

    /**
     * 查询数据
     *
     * @param query  query
     * @param page   page
     * @param size   size
     * @param show   show
     * @param tClass tClass
     * @param <T>    t
     * @param <ID>   id
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> List<T> queryDataList(CriteriaQuery<Tuple> query, Integer page, Integer size, List<String> show, Class<T> tClass, EntityManager entityManager) {
        TypedQuery<Tuple> tTypedQuery = entityManager.createQuery(query);
        if (null != page && null != size) {
            tTypedQuery.setFirstResult((page - 1) * size);
            tTypedQuery.setMaxResults(size);
        }

        List<Tuple> tList = tTypedQuery.getResultList();
        if (CollectionUtils.isEmpty(tList)) {
            return Collections.emptyList();
        }
        List<T> contents = new ArrayList<>(tList.size());
        for (Tuple tuple : tList) {
            List<TupleElement<?>> tupleElements = tuple.getElements();
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < tupleElements.size(); i++) {
                String showColumn = show.get(i);
                if (showColumn.contains(JOIN_SPLIT)) {
                    String[] showColumns = showColumn.split(JOIN_SPLIT_REGEX);
                    JSONObject filedJsonObject = jsonObject.getJSONObject(showColumns[0]);
                    if (null == filedJsonObject) {
                        filedJsonObject = new JSONObject();
                    }
                    filedJsonObject.put(showColumns[1], tuple.get(i));
                    jsonObject.put(showColumns[0], filedJsonObject);

                } else {
                    jsonObject.put(showColumn, tuple.get(i));
                }
            }
            T t = jsonObject.toJavaObject(tClass);
            contents.add(t);
        }
        return contents;
    }


    /**
     * 获取Predicate
     *
     * @param root       root
     * @param builder    builder
     * @param conditions 查询条件
     * @param <T>        实体类
     * @param <ID>       ID
     * @return .
     */
    public static <T extends IBaseEntity<ID>, ID extends Serializable> List<Predicate> getPredicates(Root<T> root, CriteriaBuilder builder, List<QueryConditionVO> conditions) {
        List<Predicate> predicates = new ArrayList<>();

        // 组装用户部门数据
        if (!(SystemEnum.SUPER_ADMIN_ID + "").equals(MdcUtils.getUserId())) {
            if (useDepartment) {
                Assert.hasText(MdcUtils.getUserDepartment(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
                predicates.add(builder.like(root.get(DEPARTMENT_COLUMN), MdcUtils.getUserDepartment() + LIKE_FIX));
            }
        }
        if (!CollectionUtils.isEmpty(conditions)) {
            Map<String, Join<T, ?>> joinMap = new HashMap<>();

            Expression<?> expression;
            for (QueryConditionVO condition : conditions) {
                String column = condition.getC();

                List<?> values = condition.getV();
                if (CollectionUtils.isEmpty(values)) {
                    continue;
                }
                // 若为关联表
                if (column.contains(JOIN_SPLIT)) {
                    String[] columns = column.split(JOIN_SPLIT_REGEX);
                    Join<T, ?> join = joinMap.get(columns[0]);
                    if (null == join) {
                        join = root.join(columns[0], JoinType.INNER);
                        joinMap.put(columns[0], join);
                    }
                    expression = join.get(columns[1]);
                } else {
                    expression = root.get(column);
                }
                addPredicate(predicates, condition, expression, builder);
            }
        }
        return predicates;
    }

    /**
     * 拼装查询条件
     *
     * @param predicates 整条件
     * @param condition  当前条件
     * @param expression 字段
     * @param builder    组装工具
     */
    private static void addPredicate(List<Predicate> predicates, QueryConditionVO condition, Expression expression,
                                     CriteriaBuilder builder) {
        if (ConditionEnum.BT.equals(condition.getD())) {
            if (condition.getV().size() == 2) {
                if (null != condition.getV().get(0) && !StringUtils.isEmpty(condition.getV().get(0).toString())) {
                    predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) condition.getV().get(0)));
                }
                if (null != condition.getV().get(1) && !StringUtils.isEmpty(condition.getV().get(1).toString())) {
                    predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) condition.getV().get(1)));
                }
            }
        } else if (ConditionEnum.IN.equals(condition.getD())) {
            Object[] hasValueValues = condition.getV().stream()
                    .filter(e -> null != e && !StringUtils.isEmpty(e.toString()))
                    .toArray();
            if (hasValueValues.length > 0) {
                if (hasValueValues.length == 1) {
                    predicates.add(builder.equal(expression, hasValueValues[0]));
                } else {
                    predicates.add(expression.in(hasValueValues));
                }
            }
        } else {
            Object value = condition.getV().get(0);
            if (null == value || StringUtils.isEmpty(value.toString())) {
                return;
            }

            switch (condition.getD()) {
                case EQ:
                    predicates.add(builder.equal(expression, value));
                    break;
                case NE:
                    predicates.add(builder.notEqual(expression, value));
                    break;
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
                    predicates.add(builder.like(expression, LIKE_FIX + value + LIKE_FIX));
                    break;
                case LL:
                    predicates.add(builder.like(expression, value + LIKE_FIX));
                    break;
                case LR:
                    predicates.add(builder.like(expression, LIKE_FIX + value));
                    break;
                default:
                    break;
            }
        }
    }

}
