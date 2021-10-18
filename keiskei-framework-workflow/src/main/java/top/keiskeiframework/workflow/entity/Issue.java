package top.keiskeiframework.workflow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import top.keiskeiframework.common.base.entity.*;
import top.keiskeiframework.common.util.data.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.workflow.enums.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;
import top.keiskeiframework.common.annotation.validate.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 卡片管理 实体类
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workflow_issue")
@ApiModel(value="Issue", description="卡片管理")
public class Issue extends TreeEntity<Long> {

    private static final long serialVersionUID = -1817563216183499388L;

    @NotBlank(message = "标题不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "标题", dataType="String")
    private String title;

    @ApiModelProperty(value = "所属计划", dataType="IssuePlan")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private IssuePlan issuePlan;

    @ApiModelProperty(value = "评论", dataType="IssueReview")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="issue_id")
    private List<IssueReview> issueReviews = new ArrayList<>();

    @ApiModelProperty(value = "操作记录", dataType="IssueOperateLog")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="issue_id")
    private List<IssueOperateLog> issueOperateLogs = new ArrayList<>();

    @NotNull(message = "附件不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "附件", dataType="IssueAttachment")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="issue_id")
    private List<IssueAttachment> issueAttachments = new ArrayList<>();

    @NotNull(message = "卡片类型不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "卡片类型", dataType="IssueType")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private IssueType issueType;

    @ApiModelProperty(value = "字段值", dataType="IssueFieldValue")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="issue_id")
    private List<IssueFieldValue> issueFieldValues = new ArrayList<>();

}
