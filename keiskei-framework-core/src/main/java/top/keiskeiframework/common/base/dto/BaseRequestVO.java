package top.keiskeiframework.common.base.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.constants.BaseConstants;
import top.keiskeiframework.common.base.entity.IBaseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

    protected String desc, asc;
    protected Boolean complete;
    protected Boolean tree;

    /**
     * 显示字段
     */
    @JsonIgnore
    protected List<String> listShow;
    protected String show;

    /**
     * 查询条件
     */
    @JsonIgnore
    protected List<QueryConditionVO> listConditions;
    protected String conditions;

    public BaseRequestVO() {
        this.complete = false;
        this.tree = true;
    }


    public void setConditions(String conditions) {
        if (!StringUtils.isEmpty(conditions)) {
            this.conditions = conditions;
            this.listConditions = JSON.parseArray(conditions, QueryConditionVO.class);
        }
    }


    public void setListConditions(List<QueryConditionVO> conditions) {
        if (!CollectionUtils.isEmpty(conditions)) {
            this.listConditions = new ArrayList<>(conditions);
            this.conditions = JSON.toJSONString(listConditions);
        }
    }

    public boolean conditionEmpty() {
        return CollectionUtils.isEmpty(listConditions);
    }


    public void setShow(String show) {
        if (!StringUtils.isEmpty(show)) {
            this.listShow = Arrays.stream(show.split(BaseConstants.SHOW_SPLIT))
                    .map(String::trim).collect(Collectors.toList());
            if (!this.listShow.contains(BaseConstants.ID_COLUMN)) {
                this.listShow.add(0, BaseConstants.ID_COLUMN);
            }
            this.show = String.join(BaseConstants.SHOW_SPLIT, this.listShow);
        }

    }

    public void setListShow(List<String> listShow) {
        if (!CollectionUtils.isEmpty(listShow)) {
            this.listShow = listShow.stream().map(String::trim).collect(Collectors.toList());
            if (this.listShow.contains(BaseConstants.ID_COLUMN)) {
                this.listShow.add(0, BaseConstants.ID_COLUMN);
            }
            this.show = String.join(BaseConstants.SHOW_SPLIT, this.listShow);
        }
    }


    public boolean showEmpty() {
        return CollectionUtils.isEmpty(listShow);
    }

    public boolean requestEmpty() {
        return this.showEmpty() && this.conditionEmpty();
    }


}
