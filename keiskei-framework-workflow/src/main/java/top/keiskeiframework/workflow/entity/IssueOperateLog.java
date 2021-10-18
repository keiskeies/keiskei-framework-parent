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
 * 卡片操作记录 实体类
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
@Table(name = "workflow_issue_operate_log")
@ApiModel(value="IssueOperateLog", description="卡片操作记录")
public class IssueOperateLog extends ListEntity<Long> {

    private static final long serialVersionUID = -4098963481163729633L;

    @ApiModelProperty(value = "操作类型", dataType="String")
    private IssueOperateLogTypeEnum type;

    @ApiModelProperty(value = "操作字段", dataType="IssueField")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private IssueField issueField;

    @NotBlank(message = "修改前不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "修改前", dataType="String")
    private String oldValue;

    @NotBlank(message = "修改后不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "修改后", dataType="String")
    private String newValue;

}
