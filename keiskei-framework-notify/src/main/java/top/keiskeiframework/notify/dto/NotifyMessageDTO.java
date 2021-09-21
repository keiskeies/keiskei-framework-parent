package top.keiskeiframework.notify.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * <p>
 * 消息推送实体
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 23:17
 */
@Data
@Builder
@ApiModel(value = "消息推送实体")
public class NotifyMessageDTO {
    @ApiModelProperty(value = "发送人", dataType = "String")
    private String from;
    @ApiModelProperty(value = "接收人", dataType = "String")
    private String to;
    @ApiModelProperty(value = "主题", dataType = "String")
    private String subject;
    @ApiModelProperty(value = "内容", dataType = "String")
    private String text;
    @ApiModelProperty(value = "是否Html", dataType = "Boolean")
    private Boolean html;
    @ApiModelProperty(value = "发送时间", dataType = "String")
    private Date sentDate;
    @ApiModelProperty(value = "抄送", dataType = "String")
    private String cc;
    @ApiModelProperty(value = "密送", dataType = "String")
    private String bcc;
    @ApiModelProperty(value = "状态", dataType = "String")
    private String status;
    @ApiModelProperty(value = "报错信息", dataType = "String")
    private String error;
    @ApiModelProperty(value = "附件", dataType = "MultipartFile[]")
    @JsonIgnore
    private MultipartFile[] multipartFiles;

    public static final String SPLIT = ",";
}
