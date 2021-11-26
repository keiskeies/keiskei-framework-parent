package top.keiskeiframework.common.base.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.enums.SystemEnum;
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
import java.util.stream.Collectors;

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
public class BaseRequestUtils<T extends ListEntity<ID>, ID extends Serializable> {
    @Value("${keiskei.use-department:false}")
    public void setUseDepartment(Boolean useDepartment) {
        BaseRequestUtils.useDepartment = useDepartment;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        BaseRequestUtils.entityManager = entityManager;
    }
    /**
     * 默认排序字段
     */
    private static final String DEFAULT_ORDER_COLUMN = "createTime";
    /**
     * 数据查询工具
     */
    private static EntityManager entityManager;
    /**
     * 是否启用部门权限
     */
    private static boolean useDepartment = false;



    /**
     * BaseEntity可显示字段
     * {@link ListEntity}
     */
    protected static final Map<String, Class<?>> LIST_ENTITY_FIELD_MAP;
    /**
     * TreeEntity可显示字段
     * {@link TreeEntity}
     */
    protected static final Map<String, Class<?>> TREE_ENTITY_FIELD_MAP;

    /**
     * 比较关系
     */
    public enum ConditionEnum {
        // =
        EQ,
        // !=
        NE,
        // >=
        GE,
        // >
        GT,
        // <=
        LE,
        // <
        LT,
        // between
        BT,
        // like
        LIKE,
        // like _%
        LL,
        // like %_
        LR,
        // in
        IN,
    }

    static {
        Field[] baseEntityFields = ListEntity.class.getDeclaredFields();
        LIST_ENTITY_FIELD_MAP = Arrays.stream(baseEntityFields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .collect(Collectors.toMap(Field::getName, Field::getType, (e1, e2) -> e2));

        Field[] treeEntityFields = TreeEntity.class.getDeclaredFields();
        TREE_ENTITY_FIELD_MAP = Arrays.stream(treeEntityFields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .collect(Collectors.toMap(Field::getName, Field::getType, (e1, e2) -> e2));
    }

    /**
     * 获取查询的指定字段
     *
     * @param root root
     * @param show show
     * @param <T>  T
     * @param <ID> ID
     * @return .
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> List<Selection<?>> getSelections(Root<T> root, List<String> show) {
        List<Selection<?>> selections = new ArrayList<>(show.size());
        for (String showColumn : show) {
            selections.add(root.get(showColumn));
        }
        return selections;
    }

    /**
     * 获取排序
     *
     * @param root   root
     * @param tClass tClass
     * @param asc    asc
     * @param desc   desc
     * @param <T>    T
     * @param <ID>   ID
     * @return .
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> List<Order> getOrders(Root<T> root, Class<T> tClass, String asc, String desc) {
        List<Order> orders = new ArrayList<>();

        if (!StringUtils.isEmpty(desc)) {
            orders.add(new OrderImpl(root.get(desc), false));
        }
        if (!StringUtils.isEmpty(asc)) {
            orders.add(new OrderImpl(root.get(asc), true));
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
    private static <T extends ListEntity<ID>, ID extends Serializable> List<Order> getOrders(Root<T> root, Class<T> tClass, List<Order> orders) {
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
            orders.add(new OrderImpl(root.get(DEFAULT_ORDER_COLUMN), false));
        }
        return orders;
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
    public static <T extends ListEntity<ID>, ID extends Serializable> List<Order> getOrders(Root<T> root, Class<T> tClass) {
        return getOrders(root, tClass, null);
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
    public static <T extends ListEntity<ID>, ID extends Serializable> Sort getSort(Class<T> tClass, String asc, String desc) {
        List<Sort.Order> orders = new ArrayList<>();

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
    private static <T extends ListEntity<ID>, ID extends Serializable> Sort getSort(Class<T> tClass, List<Sort.Order> orders) {
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
            orders.add(Sort.Order.desc(DEFAULT_ORDER_COLUMN));
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
    public static <T extends ListEntity<ID>, ID extends Serializable> Sort getSort(Class<T> tClass) {
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
    public static <T extends ListEntity<ID>, ID extends Serializable> List<T> queryDataList(CriteriaQuery<Tuple> query, List<String> show, Class<T> tClass) {
        return queryDataList(query, null, null, show, tClass);
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
    public static <T extends ListEntity<ID>, ID extends Serializable> List<T> queryDataList(CriteriaQuery<Tuple> query, Integer page, Integer size, List<String> show, Class<T> tClass) {
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
            T t = null;
            try {
                t = tClass.newInstance();
                for (int i = 0; i < tupleElements.size(); i++) {
                    Field field;
                    try {
                        field = tClass.getDeclaredField(show.get(i));
                    } catch (NoSuchFieldException | SecurityException e) {
                        field = tClass.getSuperclass().getDeclaredField(show.get(i));
                    }
                    field.setAccessible(true);
                    field.set(t, tuple.get(i));
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
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
     * @return .
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> List<Predicate> getPredicates(Root<T> root, CriteriaBuilder builder, List<QueryConditionDTO> conditions) {
        List<Predicate> predicates = new ArrayList<>();

        // 组装用户部门数据
        addDepartment(predicates, builder, root);
        if (!CollectionUtils.isEmpty(conditions)) {

            Expression<?> expression;
            for (QueryConditionDTO condition : conditions) {
                String column = condition.getColumn();

                List<Object> values = condition.getValue();
                if (CollectionUtils.isEmpty(values)) {
                    continue;
                }
                // 若为关联表
                if (column.contains(".")) {
                    String[] columns = column.split("\\.");
                    Join<?, ?> join = root.join(columns[0], JoinType.INNER);
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
    private static void addPredicate(List<Predicate> predicates, QueryConditionDTO condition, Expression expression, CriteriaBuilder builder) {
        if (ConditionEnum.BT.equals(condition.getCondition())) {
            if (condition.getValue().size() == 2) {
                if (null != condition.getValue().get(0) && !StringUtils.isEmpty(condition.getValue().get(0).toString())) {
                    predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) condition.getValue().get(0)));
                }
                if (null != condition.getValue().get(1) && !StringUtils.isEmpty(condition.getValue().get(1).toString())) {
                    predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) condition.getValue().get(1)));
                }
            }
        } else if (ConditionEnum.IN.equals(condition.getCondition())) {
            Object[] hasValueValues = condition.getValue().stream()
                    .filter(e -> null != e && !StringUtils.isEmpty(e.toString()))
                    .toArray();
            if (hasValueValues.length > 0) {
                predicates.add(expression.in(hasValueValues));
            }
        } else {
            Object value = condition.getValue().get(0);
            if (null == value || StringUtils.isEmpty(value.toString())) {
                return;
            }

            switch (condition.getCondition()) {
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
                    predicates.add(builder.like(expression, "%" + value + "%"));
                    break;
                case LL:
                    predicates.add(builder.like(expression, value + "%"));
                    break;
                case LR:
                    predicates.add(builder.like(expression, "%" + value));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 添加部门条件
     *
     * @param predicates 总条件
     * @param builder    组装工具
     * @param root       实体
     */
    private static void addDepartment(List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
        if (!(SystemEnum.SUPER_ADMIN_ID + "").equals(MdcUtils.getUserId())) {
            if (useDepartment) {
                Assert.hasText(MdcUtils.getUserDepartment(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
                predicates.add(builder.like(root.get("p"), MdcUtils.getUserDepartment() + "%"));
            }
        }
    }

    /**
     * 获取类字段
     *
     * @param tClass 实体类
     * @param <T>    实体类
     * @return .
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> Map<String, Class<?>> getFieldMap(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        Map<String, Class<?>> fieldMap = Arrays.stream(fields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .collect(Collectors.toMap(Field::getName, Field::getType, (e1, e2) -> e2));
        fieldMap.putAll(LIST_ENTITY_FIELD_MAP);
        if (tClass.getSuperclass() == TreeEntity.class) {
            fieldMap.putAll(TREE_ENTITY_FIELD_MAP);
        }
        return fieldMap;
    }


}