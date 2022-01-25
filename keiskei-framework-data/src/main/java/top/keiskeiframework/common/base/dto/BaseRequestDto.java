package top.keiskeiframework.common.base.dto;

import com.alibaba.fastjson.JSON;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.constants.BaseConstants;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <ID> .
 * @param <T>  .
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
@NoArgsConstructor
public class BaseRequestDto<T extends ListEntity<ID>, ID extends Serializable> {

    protected Set<String> fields;

    protected Set<String> getFields(Class<T> tClass) {
        if (CollectionUtils.isEmpty(fields)) {
            fields = new HashSet<>();
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
        }
        return fields;
    }

    /**
     * 查询条件
     */
    protected List<QueryConditionDTO> conditions;

    public void setConditions(String conditions) {
        if (!StringUtils.isEmpty(conditions)) {
            this.conditions = JSON.parseArray(conditions, QueryConditionDTO.class);
        }
    }

    /**
     * 组装单一条件
     *
     * @param column 字段
     * @param value  值
     */
    public BaseRequestDto(String column, Serializable value) {
        this.conditions = Collections.singletonList(
                new QueryConditionDTO(
                        column,
                        ConditionEnum.EQ,
                        Collections.singletonList(value)
                )
        );
    }

    public List<QueryConditionDTO> getConditions(Class<T> tClass) {
        if (!conditionEmpty()) {
            Set<String> fields = this.getFields(tClass);
            this.conditions.removeIf(e -> {
                if (e.getColumn().contains(".")) {
                    return !fields.contains(e.getColumn().substring(0, e.getColumn().lastIndexOf(".")));
                } else {
                    return !fields.contains(e.getColumn());
                }
            });
        }
        return this.conditions;
    }

    public void addCondition(QueryConditionDTO condition) {
        if (conditionEmpty()) {
            this.conditions = new ArrayList<>(1);
        }
        this.conditions.add(condition);
    }

    public void addConditions(List<QueryConditionDTO> conditions) {
        if (conditionEmpty()) {
            this.conditions = new ArrayList<>(conditions.size());
        }
        this.conditions.addAll(conditions);
    }

    public boolean conditionEmpty() {
        return CollectionUtils.isEmpty(conditions);
    }

    /**
     * 显示字段
     */
    protected List<String> show;

    public void setShow(String show) {
        this.show = Arrays.stream(show.split(BaseConstants.SHOW_SPLIT)).map(String::trim).collect(Collectors.toList());
        if (!this.show.contains(BaseConstants.ID_COLUMN)) {
            this.show.add(0, BaseConstants.ID_COLUMN);
        }
    }

    public void appendShow(String column) {
        if (showEmpty()) {
            this.show = new ArrayList<>(1);
        }
        this.show.add(column);
    }

    public void prependShow(String column) {
        if (showEmpty()) {
            this.show = new ArrayList<>(1);
        }
        this.show.add(0, column);
    }


    public List<String> getShow(Class<T> tClass) {
        if (!showEmpty()) {
            if (tClass.getSuperclass().equals(TreeEntity.class)) {
                if (!this.show.contains(BaseConstants.PARENT_ID_COLUMN)) {
                    this.show.add(1, BaseConstants.PARENT_ID_COLUMN);
                }
            }
        }
        if (!showEmpty()) {
            Set<String> fields = this.getFields(tClass);
            this.show.removeIf(e -> {
                if (e.contains(".")) {
                    return !fields.contains(e.substring(0, e.lastIndexOf(".")));
                } else {
                    return !fields.contains(e);
                }
            });
        }
        return this.show;
    }

    public boolean showEmpty() {
        return CollectionUtils.isEmpty(show);
    }


    public boolean requestEmpty() {
        return CollectionUtils.isEmpty(conditions) && CollectionUtils.isEmpty(show);
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "conditions=" + conditions +
                ", show=" + show +
                '}';
    }
}
