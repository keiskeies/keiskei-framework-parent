package top.keiskeiframework.common.base.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.constants.BaseConstants;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 请求数基础接口
 * </p>
 *
 * @author James Chen
 * @since 2022/11/24 17:14
 */
@Data
public class BaseRequestVO<T extends IBaseEntity<ID>, ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = -812828147724344918L;

    private String desc, asc;

    private Boolean complete;

    private Boolean tree;

    /**
     * 显示字段
     */
    protected List<String> listShow;
    private String show;

    /**
     * 查询条件
     */
    protected List<QueryConditionVO> litConditions;
    private String conditions;

    public BaseRequestVO() {
        this.complete = false;
        this.tree = true;
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> BaseRequestVO<T, ID> of(String column, Serializable value) {
        BaseRequestVO<T, ID> requestVO = new BaseRequestVO<>();
        requestVO.litConditions = new ArrayList<>(1);
        requestVO.litConditions.add(new QueryConditionVO(column, value));
        return requestVO;
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> BaseRequestVO<T, ID> of(QueryConditionVO condition) {
        BaseRequestVO<T, ID> requestVO = new BaseRequestVO<>();
        requestVO.litConditions = new ArrayList<>(1);
        requestVO.litConditions.add(condition);
        return requestVO;
    }

    public static <T extends IBaseEntity<ID>, ID extends Serializable> BaseRequestVO<T, ID> of(List<QueryConditionVO> conditions) {
        BaseRequestVO<T, ID> requestVO = new BaseRequestVO<>();
        requestVO.litConditions = conditions;
        return requestVO;
    }


    public void setConditions(String conditions) {
        this.conditions = conditions;
        if (!StringUtils.isEmpty(conditions)) {
            this.litConditions = JSON.parseArray(conditions, QueryConditionVO.class);
        }
    }

    public List<QueryConditionVO> getListConditions() {
        if (CollectionUtils.isEmpty(litConditions)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(litConditions);
    }

    public void setListConditions(List<QueryConditionVO> conditions) {
        this.litConditions = conditions.stream().map(e -> (QueryConditionVO) e).collect(Collectors.toList());
    }

    /**
     * 组装单一条件
     *
     * @param column 字段
     * @param value  值
     */
    public BaseRequestVO(String column, Serializable value) {
        this.litConditions = Collections.singletonList(
                new QueryConditionVO(
                        column,
                        ConditionEnum.EQ,
                        Collections.singletonList(value)
                )
        );
    }

    public void addCondition(QueryConditionVO condition) {
        if (conditionEmpty()) {
            this.litConditions = new ArrayList<>(1);
        }
        this.litConditions.add(condition);
    }

    public void addConditions(List<QueryConditionVO> conditions) {
        if (conditionEmpty()) {
            this.litConditions = new ArrayList<>(conditions.size());
        }
        this.litConditions.addAll(conditions);
    }

    public boolean conditionEmpty() {
        return CollectionUtils.isEmpty(litConditions);
    }


    public void setShow(String show) {
        this.listShow = Arrays.stream(show.split(BaseConstants.SHOW_SPLIT)).map(String::trim).collect(Collectors.toList());
        if (!this.listShow.contains(BaseConstants.ID_COLUMN)) {
            this.listShow.add(0, BaseConstants.ID_COLUMN);
        }
    }

    public void appendShow(String column) {
        if (showEmpty()) {
            this.listShow = new ArrayList<>(1);
        }
        this.listShow.add(column);
    }

    public void prependShow(String column) {
        if (showEmpty()) {
            this.listShow = new ArrayList<>(1);
        }
        this.listShow.add(0, column);
    }

    public boolean showEmpty() {
        return CollectionUtils.isEmpty(listShow);
    }

    public boolean requestEmpty() {
        return this.showEmpty() && this.conditionEmpty();
    }


}
