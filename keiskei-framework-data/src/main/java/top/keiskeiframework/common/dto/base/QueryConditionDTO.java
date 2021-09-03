package top.keiskeiframework.common.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.keiskeiframework.common.util.BaseRequestUtils;

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
public class QueryConditionDTO {

    @ApiModelProperty(value = "字段条件", dataType = "String")
    private String column;

    @ApiModelProperty(value = "字段条件", dataType = "String")
    private BaseRequestUtils.ConditionEnum condition;

    @ApiModelProperty(value = "字段值", dataType = "String")
    private List<Object> value;


}
