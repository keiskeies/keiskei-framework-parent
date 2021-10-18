package top.keiskeiframework.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.util.data.MoneyDeserializer;
import top.keiskeiframework.common.util.data.MoneySerializer;
import top.keiskeiframework.common.util.data.TagDeserializer;
import top.keiskeiframework.common.util.data.TagSerializer;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 表单测试 实体类
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-18 14:23:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_demo")
@ApiModel(value="Demo", description="表单测试")
public class Demo extends ListEntity<Long> {

    private static final long serialVersionUID = 2249847188988784369L;

    @NotNull(message = "整数不能为空", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "整数", dataType="Integer")
    private Integer zhengshu;

    @ApiModelProperty(value = "小数", dataType="Double")
    private Double xiaoshu;

    @ApiModelProperty(value = "金额", dataType="Long")
    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    private Long jiner;

    @ApiModelProperty(value = "日期", dataType="LocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate riqi;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(pattern = "HH:mm:ss")
    @ApiModelProperty(value = "时间", dataType="LocalTime")
    private LocalTime shijian;

    @ApiModelProperty(value = "日期时间", dataType="LocalDateTime")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime riqishijian;

    @ApiModelProperty(value = "文字", dataType="String")
    private String wenzi;

    @ApiModelProperty(value = "段落", dataType="String")
    private String duanluo;

    @ApiModelProperty(value = "文章", dataType="String")
    private String wenzhang;

    @ApiModelProperty(value = "大型文章", dataType="String")
    private String daxingwenzhang;

    @ApiModelProperty(value = "图片", dataType="String")
    private String tupian;

    @ApiModelProperty(value = "视频", dataType="String")
    private String shipin;

    @ApiModelProperty(value = "文件", dataType="String")
    private String wenjian;

    @ApiModelProperty(value = "访问次数", dataType="Integer")
    private Integer fangwencishu;

    @ApiModelProperty(value = "排序", dataType="Long")
    private Long paixu;

    @JsonDeserialize(converter = TagDeserializer.class)
    @JsonSerialize(converter = TagSerializer.class)
    private String biaoqian;

}
