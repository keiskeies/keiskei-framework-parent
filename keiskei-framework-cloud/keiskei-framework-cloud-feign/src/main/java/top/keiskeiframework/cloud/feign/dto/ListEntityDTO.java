package top.keiskeiframework.cloud.feign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.IListEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * ListEntity
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 17:04
 */
@Data
public class ListEntityDTO<ID extends Serializable> implements IListEntity<ID> {

    private static final long serialVersionUID = -6846795795755133606L;

    @NotNull(message = "请选择数据", groups = {Update.class})
    protected ID id;
    protected Integer d;
    protected String p;
    protected String createUserId;
    protected String updateUserId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", dataType = "LocalDateTime")
    protected LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", dataType = "LocalDateTime")
    protected LocalDateTime updateTime;
}
