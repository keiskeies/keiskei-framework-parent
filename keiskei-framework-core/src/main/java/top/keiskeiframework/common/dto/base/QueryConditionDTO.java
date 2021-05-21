package top.keiskeiframework.common.dto.base;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
public class QueryConditionDTO {

    @ApiModelProperty(value = "字段条件", dataType = "String")
    @JSONField(name = "column")
    private String c;

    @ApiModelProperty(value = "字段条件", dataType = "String")
    @JSONField(name = "condition")
    private ConditionEnum m;

    @ApiModelProperty(value = "字段值", dataType = "String")
    @JSONField(name = "value")
    private Object v;

    public QueryConditionDTO(String c, Object v) {
        this.c = c;
        this.v = v;
    }

    public enum ConditionEnum {
        // =
        EQ,
        // >=
        GE,
        // >
        GT,
        // <=
        LE,
        // <
        LT,
        // between
        BT,
        // like
        LIKE,
        // like _%
        LL,
        // like %_
        LR,
        // in
        IN,



    }
}
