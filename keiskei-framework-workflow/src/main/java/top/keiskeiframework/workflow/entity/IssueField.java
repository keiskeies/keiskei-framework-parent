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
 * 卡片字段 实体类
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
@Table(name = "workflow_issue_field")
@ApiModel(value="IssueField", description="卡片字段")
public class IssueField extends ListEntity<Long> {

    private static final long serialVersionUID = 6650767717420860393L;

    @NotBlank(message = "名称不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "名称", dataType="String")
    private String name;

    @NotNull(message = "类型不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "类型", dataType="String")
    private IssueFieldTypeEnum type;

    @ApiModelProperty(value = "别名", dataType="String")
    private String nickName;

    @NotBlank(message = "描述不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "描述", dataType="String")
    private String description;

    @ApiModelProperty(value = "选项", dataType="IssueFieldItem")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="issuefield_id")
    private List<IssueFieldItem> issueFieldItems = new ArrayList<>();

}
