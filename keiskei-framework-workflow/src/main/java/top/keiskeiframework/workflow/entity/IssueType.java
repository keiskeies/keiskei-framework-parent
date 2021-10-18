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
 * 卡片类型 实体类
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
@Table(name = "workflow_issue_type")
@ApiModel(value="IssueType", description="卡片类型")
public class IssueType extends ListEntity<Long> {

    private static final long serialVersionUID = 4028170921013630481L;

    @NotBlank(message = "名称不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "名称", dataType="String")
    private String name;

    @ApiModelProperty(value = "颜色", dataType="String")
    private String color;

    @ApiModelProperty(value = "类型来源", dataType="String")
    private IssueTypeTypeSourceEnum typeSource;

    @ApiModelProperty(value = "描述", dataType="String")
    private IssueTypeDescriptionEnum description;

    @ApiModelProperty(value = "字段", dataType="List<IssueField>")
    @ManyToMany(targetEntity = IssueField.class, cascade = CascadeType.DETACH)
    @JoinTable(name = "workflow_issuetype_issuefield",
        joinColumns = {@JoinColumn(name = "issuetype_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "issuefield_id", referencedColumnName = "id")})
    private List<IssueField> issueFields;

}
