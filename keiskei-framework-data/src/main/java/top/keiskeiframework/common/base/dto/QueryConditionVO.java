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
public class QueryConditionVO implements Serializable{

    private static final Long serialVersionUID = -7635199550979456815L;
    @ApiModelProperty(value = "字段条件", dataType = "String")
    private String c;

    @ApiModelProperty(value = "字段条件", dataType = "String")
    private ConditionEnum d;

    @ApiModelProperty(value = "字段值", dataType = "Serializable")
    private List<? extends Serializable> v;

    public QueryConditionVO(String c, Serializable v) {
        this.c = c;
        this.v = Collections.singletonList(v);
    }
    public QueryConditionVO(String c, List<? extends Serializable> v) {
        this.c = c;
        this.v = v;
    }

    public ConditionEnum getD() {
        if (null == this.d) {
            return ConditionEnum.EQ;
        } else {
            return d;
        }
    }


}
