package top.keiskeiframework.cloud.feign.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.cloud.feign.enums.ConditionEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
public class QueryConditionDTO implements Serializable {

    private static final Long serialVersionUID = 3265390040551843537L;
    @ApiModelProperty(value = "字段条件", dataType = "String")
    private String c;

    @ApiModelProperty(value = "字段条件", dataType = "String")
    private ConditionEnum d = ConditionEnum.EQ;

    @ApiModelProperty(value = "字段值", dataType = "String")
    private List<? extends Serializable> v;

    public QueryConditionDTO(String c, Serializable... v) {
        this.c = c;
        this.v = Arrays.asList(v);
    }
    public QueryConditionDTO(String c, ConditionEnum d, Serializable... v) {
        this.c = c;
        this.d = d;
        this.v = Arrays.asList(v);
    }

    public static String singleCondition(String c, Serializable... v) {
        return "[" + JSON.toJSONString(new QueryConditionDTO(c, v)) + "]";
    }

    public static QueryConditionDTO.QueryConditionDTOBuilder builder() {
        return new QueryConditionDTO.QueryConditionDTOBuilder();
    }

    public static class QueryConditionDTOBuilder {
        private List<QueryConditionDTO> conditions;

        QueryConditionDTOBuilder() {
        }

        public QueryConditionDTO.QueryConditionDTOBuilder conditions(
                final String c, Serializable... v) {
            if (CollectionUtils.isEmpty(conditions)) {
                this.conditions = new ArrayList<>();
            }
            this.conditions.add(new QueryConditionDTO(c, v));
            return this;
        }

        public QueryConditionDTO.QueryConditionDTOBuilder conditions(
                final String c, ConditionEnum d, Serializable... v) {
            if (CollectionUtils.isEmpty(conditions)) {
                this.conditions = new ArrayList<>();
            }
            this.conditions.add(new QueryConditionDTO(c, d, v));
            return this;
        }


        public String build() {
            if (CollectionUtils.isEmpty(conditions)) {
                return null;
            }
            return JSON.toJSONString(conditions);
        }

        @Override
        public String toString() {
            return JSON.toJSONString(conditions);
        }
    }
}
