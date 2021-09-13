package top.keiskeiframework.info.page.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.entity.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/9 02:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("conf")
@ApiModel(value = "Conf", description = "会议")
public class Conf extends BaseEntity {
    @ApiModelProperty(value = "创建时间", dataType = "String", example = "2021-09-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "创建人", dataType = "String", example = "10001")
    private String creator;

    @SortBy(desc = false)
    @ApiModelProperty(value = "排序", dataType = "Integer", example = "4")
    private Integer order;

    @ApiModelProperty(value = "会议简称", dataType = "String", example = "cvpr2020")
    private String shortName;

    @ApiModelProperty(value = "会议全称", dataType = "String", example = "Computer Vision and Pattern Recognition")
    private String fullName;

    @ApiModelProperty(value = "会议举办地点", dataType = "String", example = "Seattle, Washington")
    private String address;

    @ApiModelProperty(value = "会议简介", dataType = "String", example = "Seattle, Washington")
    private String introduce;

    @ApiModelProperty(value = "会议官方地址", dataType = "String", example = "Seattle, Washington")
    private String url;

    @ApiModelProperty(value = "会议开始日期", dataType = "String", example = "2021-09-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginDate;

    @ApiModelProperty(value = "会议结束日期", dataType = "String", example = "2021-09-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "年份", dataType = "Integer", example = "2020")
    private Integer year;

    @ApiModelProperty(value = "配置信息", dataType = "StingJson", example = "{\"rankid\":\"53a7256420f7420be8b4e0aa\",\"breadcrumb\":true,\"navigator\":[\"homepage\",\"papers\",\"roster\",\"recommend\",\"like\"],\"left\":[\"keywords\",\"leftAuthors\"],\"right\":[\"insight\"],\"paper\":{\"navigator\":[\"paper\"],\"category\":[],\"bestpaper\":true,\"download_pdf\":true,\"download_org\":[{\"key\":\"st\",\"val\":\"商汤\",\"link\":\"https://pan.baidu.com/s/1bOGF-GPakD_gIYB_3SvufA\",\"code\":\"u63q\"},{\"key\":\"ybx\",\"val\":\"优必选科技\",\"link\":\"https://pan.baidu.com/s/1jI7UFCwWcmsQurL6tRfqFA\",\"code\":\"u8x3\"},{\"key\":\"wr\",\"val\":\"微软亚洲研究院\",\"link\":\"https://pan.baidu.com/s/1aIH2eXL-ePFuqXqV0V3EPg\",\"code\":\"ju9h\"},{\"key\":\"tx\",\"val\":\"腾讯优图\",\"link\":\"https://pan.baidu.com/s/1LZ9Rg3VzZuCgV4KbkJVKQA\",\"code\":\"8yhx\"},{\"key\":\"hw\",\"val\":\"华为诺亚方舟\",\"link\":\"https://pan.baidu.com/s/1InCDF-OnnuJCLDlZFWFsbw \",\"code\":\"fk9j\"},{\"key\":\"bd\",\"val\":\"百度\",\"link\":\"https://pan.baidu.com/s/1J9bH9cu7VvHGbos48UDAgw\",\"code\":\"tky4\"},{\"key\":\"meg\",\"val\":\"旷视\",\"link\":\"https://pan.baidu.com/s/1kTBVJILZoAjhgVsLZjMcCQ\",\"code\":\"ju52\"},{\"key\":\"ks\",\"val\":\"快手\",\"link\":\"https://pan.baidu.com/s/185RZZZC7mzr6ZnARCMshFA\",\"code\":\"c9w9\"},{\"key\":\"jd\",\"val\":\"京东数科\",\"link\":\"https://pan.baidu.com/s/1zSRPK-hfmZMR1piGj2OydA\",\"code\":\"wfmr\"},{\"key\":\"qy\",\"val\":\"企业\",\"link\":\"https://pan.baidu.com/s/1cGe3fR4rUPJoB-xjO6JNRg\",\"code\":\"mtgt\"},{\"key\":\"all\",\"val\":\"all\",\"link\":\"https://pan.baidu.com/s/1HG3063D7pyyFJsS2PM0CaQ\",\"code\":\"7xbs\"}]},\"roster\":{\"navigator\":[\"authors\",\"chinese-scholar\",\"chinese-first\"],\"category\":[]},\"tdk\":{\"pageTitle\":\"CVPR2020-学术会议\",\"pageDesc\":\"CVPR 2020 会议助手涵盖会议论文、作者列表、论文解读和论文推荐，提供全方位会议信息及分析。\",\"pageKeywords\":\"CVPR2020, cvpr, cvpr学术会议, cv, 计算机视觉\"}}")
    private String config;

    @ApiModelProperty(value = "是否发布", dataType = "Boolean", example = "true")
    private Boolean isPublic;



    @Data
    @SuperBuilder
    @NoArgsConstructor
    @Document("conf_keyword")
    @ApiModel(value = "ConfKeyword", description = "会议关键字")
    public static class ConfKeyword{

        @ApiModelProperty(value = "key", dataType = "String", example = "Learning")
        private String key;
        @ApiModelProperty(value = "w", dataType = "Integer", example = "53")
        private Integer w;
        @ApiModelProperty(value = "k", dataType = "Integer", example = "53")
        private Integer k;
        @ApiModelProperty(value = "e", dataType = "Integer", example = "53")
        private Integer e;
        @ApiModelProperty(value = "n", dataType = "Integer", example = "53")
        private Integer n;
        @ApiModelProperty(value = "ids", dataType = "Array[String]", example = "[ \"5df0be543a55ac84bd7f4a78\", \"5df20fcc3a55acbe6bfcc7bc\",]")
        private List<String> ids;




    }
}
