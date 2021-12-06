package top.keiskeiframework.common.base;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.base.util.BaseRequestUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class BaseRequest<T extends ListEntity<ID>, ID extends Serializable> {
    /**
     * 默认ID字段
     */
    private static final String ID_COLUMN = "id";
    private static final String PARENT_ID_COLUMN = "parentId";
    /**
     * 展示字段分割符
     */
    private static final String SHOW_SPLIT = ",";
    private Set<String> fields;

    private Set<String> getFields(Class<T> tClass) {
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
    private List<QueryConditionDTO> conditions;
    public void setConditions(String conditions) {
        if (!StringUtils.isEmpty(conditions)) {
            this.conditions = JSON.parseArray(conditions, QueryConditionDTO.class);
        }
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
    public boolean conditionEmpty() {
        return CollectionUtils.isEmpty(conditions);
    }

    /**
     * 显示字段
     */
    private List<String> show;
    public void setShow(String show) {
        if (!StringUtils.isEmpty(show)) {
            this.show = Arrays.stream(show.split(SHOW_SPLIT)).map(String::trim).collect(Collectors.toList());
            if (!this.show.contains(ID_COLUMN)) {
                this.show.add(0, ID_COLUMN);
            }
        }
    }


    public List<String> getShow(Class<T> tClass) {
        if (!showEmpty()) {
            if (tClass.getSuperclass().equals(TreeEntity.class)) {
                if (!this.show.contains(PARENT_ID_COLUMN)) {
                    this.show.add(1, PARENT_ID_COLUMN);
                }
            }
        }
        if (!showEmpty()) {
            Set<String> fields = this.getFields(tClass);
            this.show.removeIf(e ->  !fields.contains(e));
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
