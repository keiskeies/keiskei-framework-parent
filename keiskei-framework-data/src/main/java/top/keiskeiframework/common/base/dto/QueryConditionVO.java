package top.keiskeiframework.common.base.dto;

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
@ApiModel(value = "QueryConditionDTO", description = "查询条件")
public class QueryConditionVO implements Serializable {

    private static final long serialVersionUID = -7635199550979456815L;
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

    public QueryConditionVO(String c, ConditionEnum d, Serializable v) {
        this.c = c;
        this.d = d;
        this.v = Collections.singletonList(v);
    }

    public QueryConditionVO(String c, List<? extends Serializable> v) {
        this.c = c;
        this.v = v;
    }

    public QueryConditionVO(String c, ConditionEnum d, List<? extends Serializable> v) {
        this.c = c;
        this.d = d;
        this.v = v;
    }

    public static List<QueryConditionVO> singleCondition(String c, Serializable v) {
        return Collections.singletonList(new QueryConditionVO(c, v));
    }

    public static List<QueryConditionVO> singleCondition(String c, List<? extends Serializable> v) {
        return Collections.singletonList(new QueryConditionVO(c, v));
    }

    public static List<QueryConditionVO> singleCondition(String c, ConditionEnum d, List<? extends Serializable> v) {
        return Collections.singletonList(new QueryConditionVO(c, d, v));
    }

    public ConditionEnum getD() {
        if (null == this.d) {
            return ConditionEnum.EQ;
        } else {
            return d;
        }
    }


}
