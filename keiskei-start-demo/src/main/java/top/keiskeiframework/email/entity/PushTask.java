package top.keiskeiframework.email.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.system.entity.User;

import java.util.Date;
import java.util.List;

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
@Document("push_task")
@ApiModel(value = "PushTask", description = "推送任务")
public class PushTask extends BaseEntity {

    @ApiModelProperty(value = "推送时间", dataType = "String")
    private Date sentDate;
    @ApiModelProperty(value = "推送主题", dataType = "String")
    private String subject;
    @ApiModelProperty(value = "接收人", dataType = "String")
    private List<String> toUsers;
    @ApiModelProperty(value = "推送目的", dataType = "String")
    private String purpose;
    @ApiModelProperty(value = "预期效果", dataType = "String")
    private String expectation;
    @ApiModelProperty(value = "实际推送量", dataType = "Integer")
    private Integer pushNum;
    @ApiModelProperty(value = "实际点击率", dataType = "Integer")
    private Integer clickNum;
    @DBRef
    @ApiModelProperty(value = "操作者", dataType = "User")
    private User user;
    @ApiModelProperty(value = "推送类型", dataType = "String", notes = "FIXATION/DYNAMIC")
    private PushTaskTypeEnum type;
    @ApiModelProperty(value = "推送模版", dataType = "String")
    private String template;




    public enum PushTaskTypeEnum{
        // 固定
        FIXATION,
        //动态
        DYNAMIC,
    }
}
