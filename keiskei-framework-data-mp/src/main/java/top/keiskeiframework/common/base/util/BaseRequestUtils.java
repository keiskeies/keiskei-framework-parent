package top.keiskeiframework.common.base.util;

import com.baomidou.mybatisplus.annotation.OrderBy;
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
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.MdcUtils;

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
    private final static String DEPARTMENT_COLUMN = "p";
    private final static String ID_COLUMN = "id";

    @Value("${keiskei.use-department:true}")
    public void setUseDepartment(Boolean useDepartment) {
        BaseRequestUtils.useDepartment = useDepartment;
    }

    /**
     * 是否启用部门权限
     */
    private static boolean useDepartment = true;


    public static <T extends IBaseEntity<ID>, ID extends Serializable> Set<Field> getTClassFields(Class<T> tClass) {
        Set<Field> fields = new HashSet<>();
        fields.addAll(Arrays.asList(tClass.getDeclaredFields()));
        fields.addAll(Arrays.asList(tClass.getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(tClass.getSuperclass().getSuperclass().getDeclaredFields()));
        if (tClass.getSuperclass().equals(TreeEntity.class)) {
            fields.addAll(Arrays.asList(tClass.getSuperclass().getSuperclass().getSuperclass().getDeclaredFields()));
        }
        return fields;
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> QueryWrapper<T> getQueryWrapperByConditions(
            List<QueryConditionVO> conditions, Class<T> tClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(conditions)) {
            Set<Field> fields = getTClassFields(tClass);
            convertConditions(queryWrapper, conditions, fields);
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
                Set<Field> fields = getTClassFields(tClass);
                List<QueryConditionVO> conditions = request.getConditions();
                convertConditions(queryWrapper, conditions, fields);
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

            Set<Field> fields = getTClassFields(tClass);

            if (!request.conditionEmpty()) {
                List<QueryConditionVO> conditions = request.getConditions();
                convertConditions(queryWrapper, conditions, fields);
            }
            if (!request.showEmpty()) {
                List<String> requestShows = request.getShow();
                List<String> shows = new ArrayList<>(requestShows.size());
                for (String requestShow : requestShows) {
                    String show = judgeField(requestShow, fields);
                    if (null != show) {
                        shows.add(BeanUtils.humpToUnderline(show));
                    }
                }
                queryWrapper.select(new ArrayList<>(shows).toArray(new String[]{}));
            }
            String asc = judgeField(request.getAsc(), fields);
            if (null != asc) {
                queryWrapper.orderByAsc(BeanUtils.humpToUnderline(asc));
                hasOrder = true;
            }
            String desc = judgeField(request.getDesc(), fields);
            if (null != desc) {
                queryWrapper.orderByDesc(BeanUtils.humpToUnderline(desc));
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

    private static String judgeField(String column, Set<Field> fields) {
        if (!StringUtils.isEmpty(column)) {
            Optional<Field> fieldOptional = fields.stream().filter(e -> e.getName().equals(column)).findFirst();
            if (fieldOptional.isPresent()) {
                Field field = fieldOptional.get();
                ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                if (null != manyToOne) {
                    return manyToOne.filedName();
                } else if (null != oneToOne) {
                    return oneToOne.filedName();
                } else if (null == field.getAnnotation(ManyToMany.class) && null == field.getAnnotation(OneToMany.class)) {
                    return column;
                }
            }
        }
        return null;
    }

    private static <T extends IBaseEntity<ID>, ID extends Serializable> void convertConditions(
            QueryWrapper<T> queryWrapper, List<QueryConditionVO> conditions, Set<Field> fields) {
        for (QueryConditionVO condition : conditions) {
            if (StringUtils.isEmpty(condition.getC()) || CollectionUtils.isEmpty(condition.getV())) {
                continue;
            }
            String column = condition.getC();
            String finalColumn = column;
            Optional<Field> fieldOptional = fields.stream().filter(e -> e.getName().equals(finalColumn)).findFirst();
            if (!fieldOptional.isPresent()) {
                continue;
            }
            Field field = fieldOptional.get();
            if (null != field.getAnnotation(ManyToMany.class) || null != field.getAnnotation(OneToMany.class)) {
                continue;
            }
            ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null != manyToOne) {
                column = manyToOne.filedName();
            }
            if (null != oneToOne) {
                column = oneToOne.filedName();
            }
            column = BeanUtils.humpToUnderline(column);
            if (ConditionEnum.BT.equals(condition.getD())) {
                if (condition.getV().size() != 2) {
                    continue;
                }
                if (null != condition.getV().get(0) && !StringUtils.isEmpty(condition.getV().get(0).toString())
                        && null != condition.getV().get(1) && !StringUtils.isEmpty(condition.getV().get(1).toString())) {
                    queryWrapper.between(column, condition.getV().get(0), condition.getV().get(1));
                }
            } else if (ConditionEnum.IN.equals(condition.getD())) {
                if (condition.getV().size() == 1) {
                    queryWrapper.eq(column, condition.getV().get(0));
                } else {
                    queryWrapper.in(column, condition.getV());
                }
            } else {
                Object value = condition.getV().get(0);
                if (StringUtils.isEmpty(value.toString())) {
                    continue;
                }
                switch (condition.getD()) {
                    case LIKE: queryWrapper.like(column, value); break;
                    case LL: queryWrapper.likeLeft(column, value); break;
                    case LR: queryWrapper.likeRight(column, value); break;
                    case NE: queryWrapper.ne(column, value); break;
                    case GT: queryWrapper.gt(column, value); break;
                    case GE: queryWrapper.ge(column, value); break;
                    case LT: queryWrapper.lt(column, value); break;
                    case LE: queryWrapper.le(column, value); break;
                    default: queryWrapper.eq(column, value); break;
                }
            }
        }

    }
}
