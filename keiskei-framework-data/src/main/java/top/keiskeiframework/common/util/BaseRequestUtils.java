package top.keiskeiframework.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NonNull;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
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

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 请求处理工具
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/20 18:42
 */
@Component
public class BaseRequestUtils<T extends ListEntity<ID>, ID extends Serializable> {
    private static final String IGNORE_COLUMN = "serialVersionUID";
    private static EntityManager entityManager;
    private static boolean useDepartment = false;

    @Value("${keiskei.use-department:false}")
    public void setUseDepartment(Boolean useDepartment) {
        BaseRequestUtils.useDepartment = useDepartment;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        BaseRequestUtils.entityManager = entityManager;
    }

    /**
     * BaseEntity可显示字段
     * {@link ListEntity}
     */
    protected static final Set<String> BASE_ENTITY_FIELD_SET;
    /**
     * TreeEntity可显示字段
     * {@link TreeEntity}
     */
    protected static final Set<String> TREE_ENTITY_FIELD_SET;

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
        BASE_ENTITY_FIELD_SET = Arrays.stream(baseEntityFields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .map(Field::getName).collect(Collectors.toSet());

        Field[] treeEntityFields = TreeEntity.class.getDeclaredFields();
        TREE_ENTITY_FIELD_SET = Arrays.stream(treeEntityFields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .map(Field::getName).collect(Collectors.toSet());
    }


    /**
     * 获取Specification
     *
     * @param conditions 查询条件
     * @param tClass     实体类
     * @param <T>        实体类
     * @return .
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> Specification<T> getSpecification(List<QueryConditionDTO> conditions, @NonNull Class<T> tClass) {
        Set<String> fieldSet = getFieldSet(tClass);
        return (root, query, builder) -> builder.and(getPredicates(root, builder, conditions, fieldSet).toArray(new Predicate[0]));

    }

    /**
     * 获取Specification
     *
     * @param conditions 查询条件
     * @param <T>        实体类
     * @return 。
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> Specification<T> getSpecification(List<QueryConditionDTO> conditions) {
        return (root, query, builder) -> builder.and(getPredicates(root, builder, conditions).toArray(new Predicate[0]));
    }

    /**
     * 获取CriteriaQuery
     *
     * @param conditions 。
     * @param tClass     。
     * @param <T>        。
     * @return 。
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> CriteriaQuery<T> getCriteriaQuery(List<QueryConditionDTO> conditions, @NonNull Class<T> tClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(tClass);
        if (CollectionUtils.isEmpty(conditions)) {
            return query;
        }
        Set<String> fieldSet = getFieldSet(tClass);

        List<Predicate> predicates = getPredicates(query.from(tClass), builder, conditions, fieldSet);
        return query.where(predicates.toArray(new Predicate[0]));
    }


    /**
     * 拼装query
     *
     * @param t       实体类
     * @param columns 查询部分字段
     * @param <T>     实体类
     * @return 。
     */
    public static <T extends ListEntity<ID>, ID extends Serializable> CriteriaQuery<T> getCriteriaQuery(@NonNull T t, List<String> columns) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Class<T> clazz = (Class<T>) t.getClass();

        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        List<OrderImpl> orderList = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();

        confirmCriteriaQuery(clazz.getDeclaredFields(), orderList, predicates, root, builder, t);
        confirmCriteriaQuery(ListEntity.class.getDeclaredFields(), orderList, predicates, root, builder, t);
        if (clazz.getSuperclass() == TreeEntity.class) {
            confirmCriteriaQuery(TreeEntity.class.getDeclaredFields(), orderList, predicates, root, builder, t);
        }


        if (!CollectionUtils.isEmpty(orderList)) {
            query.orderBy(orderList.toArray(new Order[0]));
        }
        if (!CollectionUtils.isEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        if (!CollectionUtils.isEmpty(columns)) {
            if (columns.size() > 1) {
                query.multiselect(columns.stream().map(root::get).collect(Collectors.toList()));
            } else {
                query.select(root.get(columns.get(0)));
            }
        }
        return query;
    }

    /**
     * 针对字段值拼装条件
     *
     * @param fields     实体类字段
     * @param orderList  排序集合
     * @param predicates 条件集合
     * @param root       实体类
     * @param builder    builder
     * @param t          实体类
     * @param <T>        实体类
     */
    private static <T extends ListEntity<ID>, ID extends Serializable> void confirmCriteriaQuery(Field[] fields, List<OrderImpl> orderList, List<Predicate> predicates, Root<T> root, CriteriaBuilder builder, T t) {
        for (Field field : fields) {
            if (IGNORE_COLUMN.equals(field.getName())) {
                continue;
            }
            if (field.getAnnotation(JsonIgnore.class) != null) {
                continue;
            }
            if (field.getAnnotation(Transient.class) != null) {
                continue;
            }
            if (field.getType().isInterface()) {
                continue;
            }
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                orderList.add(new OrderImpl(root.get(field.getName()), !sortBy.desc()));
            }
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if (null != value) {
                    predicates.add(builder.and(builder.equal(root.get(field.getName()), value)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取Predicate
     *
     * @param root       root
     * @param builder    builder
     * @param conditions 查询条件
     * @param fieldSet   实体类字段
     * @param <T>        实体类
     * @return .
     */
    private static <T extends ListEntity<ID>, ID extends Serializable> List<Predicate> getPredicates(Root<T> root, CriteriaBuilder builder, List<QueryConditionDTO> conditions, Set<String> fieldSet) {
        List<Predicate> predicates = new ArrayList<>();

        // 组装用户部门数据
        addDepartment(predicates, builder, root);

        if (!CollectionUtils.isEmpty(conditions)) {
            Expression expression;
            for (QueryConditionDTO condition : conditions) {
                String column = condition.getColumn();
                if (!fieldSet.contains(column)) {
                    continue;
                }
                List<Object> values = condition.getValue();
                if (CollectionUtils.isEmpty(values)) {
                    continue;
                }
                // 若为关联表 , 则使用左连接
                if (column.contains(".")) {
                    String[] columns = column.split("\\.");
                    Join join = root.join(columns[0], JoinType.LEFT);
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
     * 获取Predicate
     *
     * @param root       root
     * @param builder    builder
     * @param conditions 查询条件
     * @param <T>        实体类
     * @return .
     */
    private static <T extends ListEntity<ID>, ID extends Serializable> List<Predicate> getPredicates(Root<T> root, CriteriaBuilder builder, List<QueryConditionDTO> conditions) {
        List<Predicate> predicates = new ArrayList<>();

        // 组装用户部门数据
        addDepartment(predicates, builder, root);
        if (!CollectionUtils.isEmpty(conditions)) {

            Expression expression;
            for (QueryConditionDTO condition : conditions) {
                String column = condition.getColumn();

                List<Object> values = condition.getValue();
                if (CollectionUtils.isEmpty(values)) {
                    continue;
                }
                expression = root.get(column);
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
    public static void addPredicate(List<Predicate> predicates, QueryConditionDTO condition, Expression expression, CriteriaBuilder builder) {
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
    public static void addDepartment(List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
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
    public static <T extends ListEntity<ID>, ID extends Serializable> Set<String> getFieldSet(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        Set<String> fieldSet = Arrays.stream(fields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .map(Field::getName).collect(Collectors.toSet());
        fieldSet.addAll(BASE_ENTITY_FIELD_SET);
        if (tClass.getSuperclass() == TreeEntity.class) {
            fieldSet.addAll(TREE_ENTITY_FIELD_SET);
        }
        return fieldSet;
    }



}
