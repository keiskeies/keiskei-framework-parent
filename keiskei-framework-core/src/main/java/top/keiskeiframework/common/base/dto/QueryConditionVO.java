package top.keiskeiframework.common.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.base.enums.ConditionEnum;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 查询条件接口
 * </p>
 *
 * @author James Chen
 * @since 2022/11/24 17:22
 */
@Data
public class QueryConditionVO implements Serializable{

    private static final long serialVersionUID = -5919100372558700811L;
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
}
