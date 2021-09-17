package top.keiskeiframework.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import top.keiskeiframework.common.util.MdcUtils;

import java.io.Serializable;


/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -8025795001235125591L;

    @ApiModelProperty(value = "ID", dataType = "String")
    @MongoId(FieldType.OBJECT_ID)
    protected String id;

    /**
     * 数据部门
     */
    private String p = MdcUtils.getUserDepartment();


    /**
     * 图表下标
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @JsonIgnore
    protected transient String index;

    /**
     * 图表下标数量
     */
    @ApiModelProperty(hidden = true)
    @Transient
    @JsonIgnore
    protected transient Long indexNumber;


}
