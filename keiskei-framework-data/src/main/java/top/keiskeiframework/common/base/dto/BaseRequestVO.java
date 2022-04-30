package top.keiskeiframework.common.base.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.constants.BaseConstants;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.util.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <T> .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
@NoArgsConstructor
public class BaseRequestVO<T extends ListEntity<ID>, ID extends Serializable> implements Serializable{
    private static final Long serialVersionUID = -296451048174918297L;
    /**
     * 排序方式
     */
    @Setter
    @Getter
    private String desc, asc;




    /**
     * 查询条件
     */
    @Getter
    protected List<QueryConditionVO> conditions;

    public void setConditions(String conditions) {
        if (!StringUtils.isEmpty(conditions)) {
            this.conditions = JSON.parseArray(conditions, QueryConditionVO.class);
        }
    }

    /**
     * 组装单一条件
     *
     * @param column 字段
     * @param value  值
     */
    public BaseRequestVO(String column, Serializable value) {
        this.conditions = Collections.singletonList(
                new QueryConditionVO(
                        column,
                        ConditionEnum.EQ,
                        Collections.singletonList(value)
                )
        );
    }

    public void addCondition(QueryConditionVO condition) {
        if (conditionEmpty()) {
            this.conditions = new ArrayList<>(1);
        }
        this.conditions.add(condition);
    }

    public void addConditions(List<QueryConditionVO> conditions) {
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
    @Getter
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

    public boolean showEmpty() {
        return CollectionUtils.isEmpty(show);
    }

    public boolean requestEmpty() {
        return this.showEmpty() && this.conditionEmpty();
    }


    @Override
    public String toString() {
        return "BaseRequest{" +
                "conditions=" + conditions +
                ", show=" + show +
                '}';
    }
}
