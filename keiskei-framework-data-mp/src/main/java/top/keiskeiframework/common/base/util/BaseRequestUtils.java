package top.keiskeiframework.common.base.util;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.annotation.ManyToMany;
import top.keiskeiframework.common.base.annotation.ManyToOne;
import top.keiskeiframework.common.base.annotation.OneToMany;
import top.keiskeiframework.common.base.annotation.OneToOne;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.MdcUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class BaseRequestUtils<T extends IBaseEntity<ID>, ID extends Serializable> {
    private final static String DEPARTMENT_COLUMN = "p";
    private final static String ID_COLUMN = "id";

    @Value("${keiskei.use-department:false}")
    public void setUseDepartment(Boolean useDepartment) {
        BaseRequestUtils.useDepartment = useDepartment;
    }

    /**
     * 是否启用部门权限
     */
    private static boolean useDepartment = false;

    private static <T extends IBaseEntity<ID>, ID extends Serializable> Set<String> getTClassFields(Class<T> tClass) {
        Set<String> fields = new HashSet<>();
        for (Field field : tClass.getDeclaredFields()) {
            if (
                    null != field.getAnnotation(ManyToOne.class) ||
                            null != field.getAnnotation(ManyToMany.class) ||
                            null != field.getAnnotation(OneToOne.class) ||
                            null != field.getAnnotation(OneToMany.class)
             ) {
                continue;
            }
            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField && !tableField.exist()) {
                continue;
            }
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
        fields.add(ID_COLUMN);
        return fields;
    }

    private static void validFields(Set<String> fields, List<QueryConditionVO> conditions) {
        for (QueryConditionVO conditionVO : conditions) {
            if (!fields.contains(conditionVO.getC())) {
                throw new BizException(BizExceptionEnum.ERROR.getCode(), "字段:" + conditionVO.getC() + " 不存在，请检查");
            }
        }
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> QueryWrapper<T> getQueryWrapperByConditions(
            List<QueryConditionVO> conditions, Class<T> tClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(conditions)) {
            Set<String> fields = getTClassFields(tClass);
            validFields(fields, conditions);
            convertConditions(queryWrapper, conditions);
        }

        if (useDepartment) {
            queryWrapper.likeRight(DEPARTMENT_COLUMN, MdcUtils.getUserDepartment());
        }
        return queryWrapper;
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> QueryWrapper<T> getQueryWrapperByConditions(
            BaseRequestVO<T, ID> request, Class<T> tClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (null != request) {
            if (!request.conditionEmpty()) {
                Set<String> fields = getTClassFields(tClass);
                List<QueryConditionVO> conditions = request.getConditions();
                validFields(fields, conditions);
                convertConditions(queryWrapper, conditions);
            }
        }

        if (useDepartment) {
            queryWrapper.likeRight(DEPARTMENT_COLUMN, MdcUtils.getUserDepartment());
        }
        return queryWrapper;
    }


    public static <T extends IBaseEntity<ID>, ID extends Serializable> QueryWrapper<T> getQueryWrapper(
            BaseRequestVO<T, ID> request, Class<T> tClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        boolean hasOrder = false;
        if (null != request) {

            Set<String> fields = getTClassFields(tClass);

            if (!CollectionUtils.isEmpty(fields)) {
                if (!request.conditionEmpty()) {
                    List<QueryConditionVO> conditions = request.getConditions();
                    validFields(fields, conditions);
                    convertConditions(queryWrapper, conditions);
                }
                if (!request.showEmpty()) {
                    List<String> shows = request.getShow();
                    shows.removeIf(e -> !fields.contains(e));
                    queryWrapper.select(shows.stream().map(BeanUtils::humpToUnderline)
                            .collect(Collectors.toList()).toArray(new String[]{}));
                }
            }
            if (!StringUtils.isEmpty(request.getAsc()) && fields.contains(request.getAsc())) {
                queryWrapper.orderByAsc(BeanUtils.humpToUnderline(request.getAsc()));
                hasOrder = true;
            }
            if (!StringUtils.isEmpty(request.getDesc()) && fields.contains(request.getDesc())) {
                queryWrapper.orderByDesc(BeanUtils.humpToUnderline(request.getDesc()));
                hasOrder = true;
            }
        }

        if (!hasOrder) {
            for (Field field : tClass.getDeclaredFields()) {
                OrderBy orderBy = field.getAnnotation(OrderBy.class);
                if (null != orderBy) {
                    queryWrapper.orderBy(true, orderBy.asc(), BeanUtils.humpToUnderline(field.getName()));
                    hasOrder = true;
                }
            }
        }
        if (!hasOrder) {
            queryWrapper.orderByDesc(ID_COLUMN);
        }
        return queryWrapper;
    }

    private static <T extends IBaseEntity<ID>, ID extends Serializable> void convertConditions(
            QueryWrapper<T> queryWrapper, List<QueryConditionVO> conditions
    ) {
        for (QueryConditionVO condition : conditions) {
            String column = BeanUtils.humpToUnderline(condition.getC());
            if (ConditionEnum.BT.equals(condition.getD())) {
                if (condition.getV().size() == 2) {
                    if (null != condition.getV().get(0) && !StringUtils.isEmpty(condition.getV().get(0).toString())
                            && null != condition.getV().get(1) && !StringUtils.isEmpty(condition.getV().get(1).toString())) {
                        queryWrapper.between(column, condition.getV().get(0), condition.getV().get(1));
                    }
                }
            } else if (ConditionEnum.IN.equals(condition.getD())) {
                Object[] hasValueValues = condition.getV().stream()
                        .filter(e -> null != e && !StringUtils.isEmpty(e.toString()))
                        .toArray();
                if (hasValueValues.length > 0) {
                    if (hasValueValues.length == 1) {
                        queryWrapper.eq(column, condition.getV().get(0));
                    } else {
                        queryWrapper.in(column, condition.getV());
                    }
                }
            } else {
                Object value = condition.getV().get(0);
                if (null == value || StringUtils.isEmpty(value.toString())) {
                    continue;
                }
                switch (condition.getD()) {
                    case LIKE:
                        queryWrapper.like(column, value);
                        break;
                    case LL:
                        queryWrapper.likeLeft(column, value);
                        break;
                    case LR:
                        queryWrapper.likeRight(column, value);
                        break;
                    case NE:
                        queryWrapper.ne(column, value);
                        break;
                    case GT:
                        queryWrapper.gt(column, value);
                        break;
                    case GE:
                        queryWrapper.ge(column, value);
                        break;
                    case LT:
                        queryWrapper.lt(column, value);
                        break;
                    case LE:
                        queryWrapper.le(column, value);
                        break;
                    default:
                        queryWrapper.eq(column, value);
                        break;
                }
            }
        }

    }
}
