package top.keiskeiframework.email.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息推送记录
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("push_record")
@ApiModel(value = "PushRecord", description = "推送记录")
public class PushRecord extends BaseEntity {

    @ApiModelProperty(value = "邮件主题", dataType = "String")
    private String subject;
    @ApiModelProperty(value = "发送人账号", dataType = "String")
    private String toUser;
    @ApiModelProperty(value = "发送时间", dataType = "String")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentDate;
    @ApiModelProperty(value = "状态", dataType = "String", notes = "SUCCESS/FAIL")
    private PushStatusEnum status;

    public enum PushStatusEnum {
        // 成功
        SUCCESS,
        // 失败
        FAIL
    }
}
