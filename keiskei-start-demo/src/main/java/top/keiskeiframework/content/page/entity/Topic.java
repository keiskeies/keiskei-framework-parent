package top.keiskeiframework.content.page.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 科技资讯实体类
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/9 00:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("Aminer_topic")
@ApiModel(value = "Topic", description = "Topic")
public class Topic extends BaseEntity {


    @ApiModelProperty(value = "英文标题", dataType = "String", example = "Last Dancing")
    private String name;

    @ApiModelProperty(value = "中文标题", dataType = "String", example = "霸王别姬")
    private String nameZh;

    @ApiModelProperty(value = "中文标题拼音", dataType = "String", example = "bawangbieji")
    private String nameZhPinyin;

    @ApiModelProperty(value = "英文简介", dataType = "String", example = "Last Dancing")
    private String def;

    @ApiModelProperty(value = "英文简介", dataType = "String", example = "霸王别姬")
    private String defZh;

    @ApiModelProperty(value = "别名", dataType = "String", example = "Last Dancing")
    private String alias;

    @ApiModelProperty(value = "分类", dataType = "Array", example = "[\"10010\", \"10011\"]")
    private List<String> channel;

    @ApiModelProperty(value = "原topic", dataType = "String", example = "10009")
    private String oldtopic;

    @ApiModelProperty(value = "更新时间", dataType = "String", example = "2021-09-01 00:00:00")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否发布", dataType = "Boolean", example = "false")
    @Field("is_public")
    private Boolean isPublic;

    @ApiModelProperty(value = "历史更新时间记录", dataType = "Array")
    private List<TopicLike> like;

    @ApiModelProperty(value = "更新次数", dataType = "Integer", example = "7")
    private Integer numLike;

    @ApiModelProperty(value = "必读推荐", dataType = "Array")
    private List<TopicMustReading> mustReading;

    @ApiModelProperty(value = "必读推荐数量", dataType = "Integer", example = "7")
    private Integer mustReadingCount;

    @ApiModelProperty(value = "阅读次数", dataType = "Integer", example = "7")
    private Integer numView;

    @ApiModelProperty(value = "引用次数", dataType = "Integer", example = "7")
    private Integer citationCount;

    @ApiModelProperty(value = "pdf数量", dataType = "Integer", example = "7")
    private Integer pdfCount;

    @ApiModelProperty(value = "ppt数量", dataType = "Integer", example = "7")
    private Integer pptCount;

    @ApiModelProperty(value = "统计数量", dataType = "Integer", example = "7")
    private Integer statsTotal;

    @ApiModelProperty(value = "统计时间", dataType = "String", example = "2021-09-01 00:00:00")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime statsUpdatedTime;


    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @Document("topic_like")
    @ApiModel(value = "TopicLike", description = "历史更新时间记录")
    public static class TopicLike extends BaseEntity {

        @ApiModelProperty(value = "更新时间", dataType = "String", example = "2021-09-01 00:00:00")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updateTime;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @Document("topic_must_reading")
    @ApiModel(value = "TopicMustReading", description = "推荐必读")
    public static class TopicMustReading extends BaseEntity {


        @ApiModelProperty(value = "pid", dataType = "String", example = "10010")
        private String pid;

        @ApiModelProperty(value = "推荐原因", dataType = "String", example = "Last Dancing")
        private String reason;

        @ApiModelProperty(value = "排序", dataType = "Integer", example = "2")
        @SortBy(desc = false)
        private Integer order;

        @ApiModelProperty(value = "年份", dataType = "Integer", example = "1991")
        private Integer year;

        @ApiModelProperty(value = "选择", dataType = "Boolean", example = "true")
        private Boolean selected;
    }


}
