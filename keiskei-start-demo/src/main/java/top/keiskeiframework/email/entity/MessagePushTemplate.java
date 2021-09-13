package top.keiskeiframework.email.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.BaseEntity;

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
@Document("message_push_template")
@ApiModel(value = "MessagePushTemplate", description = "消息模板")
public class MessagePushTemplate extends BaseEntity {

    @ApiModelProperty(value = "模板内容", dataType = "String", example = "<p>${title}</p>")
    private String content;
}
