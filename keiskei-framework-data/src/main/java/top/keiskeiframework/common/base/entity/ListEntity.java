package top.keiskeiframework.common.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import top.keiskeiframework.common.aop.AbstractAuditorAware;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:12:51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ListEntity<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = -8025795001235125591L;

    protected ID id;


    /**
     * 数据所属部门
     */
    @TableField(fill = FieldFill.INSERT)
    protected String p;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    protected Integer d;

    /**
     * 数据创建人
     * 数据初始化来源 {@link AbstractAuditorAware}
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createUserId;

    /**
     * 最后修改人
     * 数据初始化来源 {@link AbstractAuditorAware}
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Long updateUserId;


    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", dataType = "LocalDateTime")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", dataType = "LocalDateTime")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;


}
