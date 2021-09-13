package top.keiskeiframework.email.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.BaseEntity;

import java.util.Date;

/**
 * <p>
 * 消息推送实体类
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 23:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("message_push")
@ApiModel(value = "MessagePush", description = "消息推送")
public class MessagePush extends BaseEntity {

    @ApiModelProperty(value = "接收人", dataType = "String")
    private String to;
    @ApiModelProperty(value = "主题", dataType = "String")
    private String subject;
    @ApiModelProperty(value = "内容", dataType = "String")
    private String text;
    @ApiModelProperty(value = "发送时间", dataType = "String")
    private Date sentDate;
    @ApiModelProperty(value = "抄送", dataType = "String")
    private String cc;
    @ApiModelProperty(value = "密送", dataType = "String")
    private String bcc;
}
