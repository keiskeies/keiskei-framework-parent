package top.keiskeiframework.common.base.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 查询条件
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/14 16:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "QueryConditionDTO", description = "查询条件")
public class QueryConditionVO {

    @ApiModelProperty(value = "字段条件", dataType = "String")
    @JSONField(alternateNames = "c")
    private String column;

    @ApiModelProperty(value = "字段条件", dataType = "String")
    @JSONField(alternateNames = "d")
    private ConditionEnum condition;

    @ApiModelProperty(value = "字段值", dataType = "String")
    @JSONField(alternateNames = "v")
    private List<? extends Serializable> value;

    public QueryConditionVO(String column, Serializable value) {
        this.column = column;
        this.value = Collections.singletonList(value);
    }
    public QueryConditionVO(String column, List<? extends Serializable> value) {
        this.column = column;
        this.value = value;
    }

    public ConditionEnum getCondition() {
        if (null == this.condition) {
            return ConditionEnum.EQ;
        } else {
            return condition;
        }
    }


}
