package top.keiskeiframework.common.base.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.MdcUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 请求处理工具
 * </p>
 *
 * @param <T> .
 * @author v_chenjiamin
 * @since 2021/5/20 18:42
 */
@Component
public class BaseRequestUtils<T extends ListEntity> {
    private final static String JOIN_SPLIT = ".";
    private final static String JOIN_SPLIT_REGEX = "\\.";
    private final static String DEPARTMENT_COLUMN = "p";
    private final static String PARENT_COLUMN = "parentId";
    private final static String CREATE_TIME_COLUMN = "create_time";

    @Value("${keiskei.use-department:false}")
    public void setUseDepartment(Boolean useDepartment) {
        BaseRequestUtils.useDepartment = useDepartment;
    }

    /**
     * 是否启用部门权限
     */
    private static boolean useDepartment = false;
    private static <T extends ListEntity> Set<String> getTClassFields(Class<T> tClass) {
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

    public static <T extends ListEntity> QueryWrapper<T> getQueryWrapperByConditions(List<QueryConditionVO> conditions,
                                                                         Class<T> tClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(conditions)) {
            Set<String> fields = getTClassFields(tClass);
            conditions.removeIf(e -> !fields.contains(e.getColumn()));
            convertConditions(queryWrapper, conditions);
        }

        if (useDepartment) {
            queryWrapper.likeRight(DEPARTMENT_COLUMN, MdcUtils.getUserDepartment());
        }
        return queryWrapper;
    }

    public static <T extends ListEntity> QueryWrapper<T> getQueryWrapper(BaseRequestVO<T> request, Class<T> tClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        boolean hasOrder = false;
        if (null != request) {

            Set<String> fields = getTClassFields(tClass);

            if (!CollectionUtils.isEmpty(fields)) {
                if (!request.conditionEmpty()) {
                    List<QueryConditionVO> conditions = request.getConditions();
                    conditions.removeIf(e -> !fields.contains(e.getColumn()));
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
            queryWrapper.orderByDesc(CREATE_TIME_COLUMN);
        }
        if (useDepartment) {
            queryWrapper.likeRight(DEPARTMENT_COLUMN, MdcUtils.getUserDepartment());
        }
        return queryWrapper;
    }

    private static <T extends ListEntity> void convertConditions(
            QueryWrapper<T> queryWrapper, List<QueryConditionVO> conditions
    ) {
        for (QueryConditionVO condition : conditions) {
            String column = BeanUtils.humpToUnderline(condition.getColumn());
            if (ConditionEnum.BT.equals(condition.getCondition())) {
                if (condition.getValue().size() == 2) {
                    if (null != condition.getValue().get(0) && !StringUtils.isEmpty(condition.getValue().get(0).toString())
                            && null != condition.getValue().get(1) && !StringUtils.isEmpty(condition.getValue().get(1).toString())) {
                        queryWrapper.between(column, condition.getValue().get(0), condition.getValue().get(1));
                    }
                }
            } else if (ConditionEnum.IN.equals(condition.getCondition())) {
                Object[] hasValueValues = condition.getValue().stream()
                        .filter(e -> null != e && !StringUtils.isEmpty(e.toString()))
                        .toArray();
                if (hasValueValues.length > 0) {
                    if (hasValueValues.length == 1) {
                        queryWrapper.eq(column, condition.getValue().get(0));
                    } else {
                        queryWrapper.in(column, condition.getValue());
                    }
                }
            } else {
                Object value = condition.getValue().get(0);
                if (null == value || StringUtils.isEmpty(value.toString())) {
                    continue;
                }
                switch (condition.getCondition()) {
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
