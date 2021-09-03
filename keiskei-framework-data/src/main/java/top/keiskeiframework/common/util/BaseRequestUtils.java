package top.keiskeiframework.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;

import java.lang.reflect.Field;
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
public class BaseRequestUtils {
    protected static final String IGNORE_COLUMN = "serialVersionUID";

    /**
     * BaseEntity可显示字段
     * {@link BaseEntity}
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
        Field[] baseEntityFields = BaseEntity.class.getDeclaredFields();
        BASE_ENTITY_FIELD_SET = Arrays.stream(baseEntityFields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .map(Field::getName).collect(Collectors.toSet());

        Field[] treeEntityFields = TreeEntity.class.getDeclaredFields();
        TREE_ENTITY_FIELD_SET = Arrays.stream(treeEntityFields)
                .filter(e -> e.getAnnotation(JsonIgnore.class) == null)
                .map(Field::getName).collect(Collectors.toSet());
    }

    public static <T> Query getQuery(List<QueryConditionDTO> conditions, Class<T> tClass) {
        Set<String> fieldSet = getFieldSet(tClass);
        Query query = new Query();
        confirmQuery(query, conditions, fieldSet);
        return query;
    }


    /**
     * 针对字段值拼装条件
     *
     * @param fields 实体类字段
     * @param query  查询条件
     * @param t      实体类
     * @param <T>    实体类
     */
    private static <T> void confirmCriteriaQuery(Field[] fields, Query query, T t) {
        for (Field field : fields) {
            if (IGNORE_COLUMN.equals(field.getName())) {
                continue;
            }
            if (field.getAnnotation(JsonIgnore.class) != null) {
                continue;
            }
            if (field.getType().isInterface()) {
                continue;
            }
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                query.with(Sort.by(sortBy.desc() ? Sort.Direction.DESC : Sort.Direction.ASC, field.getName()));
            }
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if (null != value) {
                    query.addCriteria(Criteria.where(field.getName()).is(value));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取Predicate
     *
     * @param query      query
     * @param conditions 查询条件
     * @param fieldSet   实体类字段
     * @param <T>        实体类
     */
    private static <T> void confirmQuery(Query query, List<QueryConditionDTO> conditions, Set<String> fieldSet) {
        if (!CollectionUtils.isEmpty(conditions)) {
            for (QueryConditionDTO condition : conditions) {
                String column = condition.getColumn();
                if (!fieldSet.contains(column)) {
                    continue;
                }
                List<Object> values = condition.getValue();
                if (CollectionUtils.isEmpty(values)) {
                    continue;
                }

                addCriteriaDefinition(query, condition);
            }
        }
    }


    /**
     * 拼装查询条件
     *
     * @param query     整条件
     * @param condition 当前条件
     */
    public static void addCriteriaDefinition(Query query, QueryConditionDTO condition) {
        if (ConditionEnum.BT.equals(condition.getCondition())) {
            if (condition.getValue().size() == 2) {
                if (null != condition.getValue().get(0) && !StringUtils.isEmpty(condition.getValue().get(0).toString())) {

                    query.addCriteria(
                            Criteria.where(condition.getColumn()).gte(condition.getValue().get(0))
                    );
                }
                if (null != condition.getValue().get(1) && !StringUtils.isEmpty(condition.getValue().get(1).toString())) {
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).lte(condition.getValue().get(1))
                    );
                }
            }
        } else if (ConditionEnum.IN.equals(condition.getCondition())) {
            Object[] hasValueValues = condition.getValue().stream()
                    .filter(e -> null != e && !StringUtils.isEmpty(e.toString()))
                    .toArray();
            if (hasValueValues.length > 0) {
                query.addCriteria(
                        Criteria.where(condition.getColumn()).in(condition.getValue())
                );
            }
        } else {
            Object value = condition.getValue().get(0);
            if (null == value || StringUtils.isEmpty(value.toString())) {
                return;
            }

            switch (condition.getCondition()) {
                case NE:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).ne(condition.getValue().get(0))
                    );
                    break;
                case GT:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).gt(condition.getValue().get(0))
                    );
                    break;
                case GE:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).gte(condition.getValue().get(0))
                    );
                    break;
                case LT:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).lt(condition.getValue().get(0))
                    );
                    break;
                case LE:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).lte(condition.getValue().get(0))
                    );
                    break;
                case LIKE:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).regex(".*?" + condition.getValue().get(0) + ".*")
                    );
                    break;
                case LL:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).regex(condition.getValue().get(0) + ".*")
                    );
                    break;
                case LR:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).regex(".*?" + condition.getValue().get(0))
                    );
                    break;
                default:
                    query.addCriteria(
                            Criteria.where(condition.getColumn()).is(condition.getValue().get(0))
                    );
                    break;
            }
        }
    }
//
//    /**
//     * 添加部门条件
//     *
//     * @param query 总条件
//     */
//    public static void addDepartment(Query query) {
//        TokenUser tokenUser = SecurityUtils.getSessionUser();
//        if (SystemEnum.SUPER_ADMIN_ID != tokenUser.getId()) {
//            Assert.hasText(tokenUser.getDepartment(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
//            query.addCriteria(
//                    Criteria.where("p").regex(tokenUser.getDepartment() + ".*")
//            );
//        }
//    }

    /**
     * 获取类字段
     *
     * @param tClass 实体类
     * @param <T>    实体类
     * @return .
     */
    public static <T> Set<String> getFieldSet(Class<T> tClass) {
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
