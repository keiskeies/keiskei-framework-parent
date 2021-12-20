package top.keiskeiframework.common.base.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.keiskeiframework.common.base.enums.ConditionEnum;
import top.keiskeiframework.common.base.util.BaseRequestUtils;

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
public class QueryConditionDTO {

    @ApiModelProperty(value = "字段条件", dataType = "String")
    private String column;

    @ApiModelProperty(value = "字段条件", dataType = "String")
    private ConditionEnum condition;

    @ApiModelProperty(value = "字段值", dataType = "String")
    private List<Object> value;


}
