package top.keiskeiframework.common.base.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.annotation.SortBy;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
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
public class BaseRequestUtils<T extends IBaseEntity<ID>, ID extends Serializable> {
    private static final String IGNORE_COLUMN = "serialVersionUID";
    private static final int BT_SIZE = 2;

    /**
     * BaseEntity可显示字段
     * {@link ListEntity}
     */
    private static final Set<String> BASE_ENTITY_FIELD_SET;
    /**
     * TreeEntity可显示字段
     * {@link TreeEntity}
     */
    private static final Set<String> TREE_ENTITY_FIELD_SET;

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

    private static <T extends IBaseEntity<ID>, ID extends Serializable> Set<String> getTClassFields(Class<T> tClass) {
        Set<String> fields = new HashSet<>();
        for (Field field : tClass.getDeclaredFields()) {
            fields.add(field.getName());
        }
        for (Field field : tClass.getSuperclass().getDeclaredFields()) {
            fields.add(field.getName());
        }
        if (tClass.getSuperclass().equals(TreeEntity.class)) {
            for (Field field : tClass.getSuperclass().getSuperclass().getDeclaredFields()) {
                fields.add(field.getName());
            }
        }
        return fields;
    }

    private static void validFields(Set<String> fields, List<QueryConditionVO> conditions) {
        for (QueryConditionVO conditionVO : conditions) {
            if (!fields.contains(conditionVO.getC())) {
                throw new BizException(BizExceptionEnum.ERROR.getCode(), "字段:" + conditionVO.getC() + " 不存在，请检查");
            }
        }
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> Query getQuery(BaseRequestVO<T, ID> request, Class<T> tClass) {
        return getQuery(request.getConditions(), request.getShow().toArray(new String[0]), tClass);
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> Query getQuery(List<QueryConditionVO> conditions, String[] show, Class<T> tClass) {
        Set<String> fieldSet = getFieldSet(tClass);
        Query query = new Query();
        Field[] fields = tClass.getDeclaredFields();
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
            } else {
                query.with(Sort.by(Sort.Direction.DESC, "id"));
            }
        }

        confirmQuery(query, conditions, fieldSet, show);
        return query;
    }


    /**
     * 获取Predicate
     *
     * @param query      query
     * @param conditions 查询条件
     * @param fieldSet   实体类字段
     */
    private static void confirmQuery(Query query, List<QueryConditionVO> conditions, Set<String> fieldSet, String[] show) {
        if (!CollectionUtils.isEmpty(conditions)) {
            for (QueryConditionVO condition : conditions) {
                String column = condition.getC();
                if (!fieldSet.contains(column)) {
                    continue;
                }
                List<? extends Serializable> values = condition.getV();
                if (CollectionUtils.isEmpty(values)) {
                    continue;
                }
                addCriteriaDefinition(query, condition);
            }
        }
        if (null != show) {
            org.springframework.data.mongodb.core.query.Field findFields = query.fields();
            Arrays.stream(show).filter(fieldSet::contains).forEach(findFields::include);
        }
    }


    /**
     * 拼装查询条件
     *
     * @param query     整条件
     * @param condition 当前条件
     */
    private static void addCriteriaDefinition(Query query, QueryConditionVO condition) {
        if (ConditionEnum.BT.equals(condition.getD())) {
            if (BT_SIZE == condition.getV().size()) {
                if (null != condition.getV().get(0) && !StringUtils.isEmpty(condition.getV().get(0).toString())) {

                    query.addCriteria(
                            Criteria.where(condition.getC()).gte(condition.getV().get(0))
                    );
                }
                if (null != condition.getV().get(1) && !StringUtils.isEmpty(condition.getV().get(1).toString())) {
                    query.addCriteria(
                            Criteria.where(condition.getC()).lte(condition.getV().get(1))
                    );
                }
            }
        } else if (ConditionEnum.IN.equals(condition.getD())) {
            Object[] hasValueValues = condition.getV().stream()
                    .filter(e -> null != e && !StringUtils.isEmpty(e.toString()))
                    .toArray();
            if (hasValueValues.length > 0) {
                query.addCriteria(
                        Criteria.where(condition.getC()).in(condition.getV())
                );
            }
        } else {
            Object value = condition.getV().get(0);
            if (null == value || StringUtils.isEmpty(value.toString())) {
                return;
            }

            switch (condition.getD()) {
                case NE:
                    query.addCriteria(
                            Criteria.where(condition.getC()).ne(condition.getV().get(0))
                    );
                    break;
                case GT:
                    query.addCriteria(
                            Criteria.where(condition.getC()).gt(condition.getV().get(0))
                    );
                    break;
                case GE:
                    query.addCriteria(
                            Criteria.where(condition.getC()).gte(condition.getV().get(0))
                    );
                    break;
                case LT:
                    query.addCriteria(
                            Criteria.where(condition.getC()).lt(condition.getV().get(0))
                    );
                    break;
                case LE:
                    query.addCriteria(
                            Criteria.where(condition.getC()).lte(condition.getV().get(0))
                    );
                    break;
                case LIKE:
                    query.addCriteria(
                            Criteria.where(condition.getC()).regex(".*?" + condition.getV().get(0) + ".*?")
                    );
                    break;
                case LL:
                    query.addCriteria(
                            Criteria.where(condition.getC()).regex(condition.getV().get(0) + ".*?")
                    );
                    break;
                case LR:
                    query.addCriteria(
                            Criteria.where(condition.getC()).regex(".*?" + condition.getV().get(0))
                    );
                    break;
                default:
                    query.addCriteria(
                            Criteria.where(condition.getC()).is(condition.getV().get(0))
                    );
                    break;
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
