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
    private Date sentDate;
    @ApiModelProperty(value = "状态", dataType = "String", notes = "SUCCESS/FAIL")
    private PushStatusEnum status;

    public enum PushStatusEnum {
        // 成功
        SUCCESS,
        // 失败
        FAIL
    }
}
