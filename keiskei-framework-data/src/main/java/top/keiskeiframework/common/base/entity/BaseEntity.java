package top.keiskeiframework.common.base.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import top.keiskeiframework.common.util.data.ObjectIdSerializer;

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

    @Id
    @ApiModelProperty(value = "ID", dataType = "String")
    protected ObjectId id;

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
