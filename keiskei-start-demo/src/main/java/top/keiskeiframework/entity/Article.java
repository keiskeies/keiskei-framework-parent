package top.keiskeiframework.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import top.keiskeiframework.common.base.entity.BaseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
@Document("articles")
@ApiModel(value = "Article", description = "科技资讯")
public class Article extends BaseEntity {

    @ApiModelProperty(value = "关键词", dataType = "Array", example = "[\"数码\"]")
    private List<String> keywords;

    @ApiModelProperty(value = "标题", dataType = "String", example = "小米股价")
    private String title;

    @ApiModelProperty(value = "作者", dataType = "String", example = "雷军")
    private String author;

    @ApiModelProperty(value = "图片", dataType = "String", example = "https://zhengwen.aminer.cn/sYtrAF7HWz7le")
    private String image;

    @ApiModelProperty(value = "是否置顶", dataType = "Boolean", example = "false")
    private Boolean isTop;

    @ApiModelProperty(value = "是否可下载", dataType = "Boolean", example = "false")
    private Boolean isDownload;

    @ApiModelProperty(value = "是否草稿", dataType = "Boolean", example = "false")
    private Boolean isDraft;

    @ApiModelProperty(value = "创建时间", dataType = "String", example = "2021-09-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", dataType = "String", example = "2021-09-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "浏览次数", dataType = "Integer", example = "3")
    private Integer view;

    @ApiModelProperty(value = "分享次数", dataType = "Integer", example = "3")
    private Integer share;

    @ApiModelProperty(value = "修改次数", dataType = "Integer", example = "3")
    private Integer like;

    @ApiModelProperty(value = "pdf名称", dataType = "String", example = "9527.pdf")
    private String pdfname;

    @ApiModelProperty(value = "微信链接", dataType = "String", example = "https://mp.weixin.qq.com/s/uPmVmkVIc7fQh-U9DgeC_w")
    private String wxlink;

    @ApiModelProperty(value = "微信链接", dataType = "String", example = "https://mp.weixin.qq.com/s/uPmVmkVIc7fQh-U9DgeC_w")
    private String wxshow;

    @ApiModelProperty(value = "分类", dataType = "String", example = "资讯")
    private String classify;

    @ApiModelProperty(name = "abstract", value = "简介", dataType = "String", example = "资讯")
    @Field("abstract")
    @JSONField(name = "abstract")
    private String abstract_;

    @ApiModelProperty(value = "raw", dataType = "ArticleRaw")
    private ArticleRaw raw;


    @Data
    @ApiModel(value = "ArticleRaw", description = "")
    public static class ArticleRaw {

        @ApiModelProperty(value = "blocks", dataType = "Array")
        private List<ArticleRawBlock> blocks;
        @ApiModelProperty(value = "entityMap", dataType = "Map")
        private Map<String, ArticleRawEntity> entityMap;


        @Data
        @ApiModel(value = "ArticleRawEntity", description = "")
        public static class ArticleRawEntity {
            @ApiModelProperty(value = "type", dataType = "String", example = "IMAGE")
            private String type;
            @ApiModelProperty(value = "mutability", dataType = "String", example = "IMMUTABLE")
            private String mutability;
            @ApiModelProperty(value = "data", dataType = "ArticleRawEntityData", example = "IMMUTABLE")
            private ArticleRawEntityData data;

            @Data
            @ApiModel(value = "ArticleRawEntityData", description = "")
            public static class ArticleRawEntityData {
                @ApiModelProperty(value = "meta", dataType = "Object", example = "IMAGE")
                private String meta;
                @ApiModelProperty(value = "url", dataType = "String", example = "https://zhengwen.aminer.cn/YbmO7zdNfWQij")
                private String url;

            }

        }

        @Data
        @ApiModel(value = "ArticleRawBlock", description = "")
        public static class ArticleRawBlock {


            @ApiModelProperty(value = "key", dataType = "String", example = "3hdfo")
            private String key;
            @ApiModelProperty(value = "text", dataType = "String", example = "3hdfo")
            private String text;
            @ApiModelProperty(value = "type", dataType = "String", example = "unstyled")
            private String type;
            @ApiModelProperty(value = "depth", dataType = "Integer", example = "2")
            private Integer depth;
            @ApiModelProperty(value = "inlineStyleRanges", dataType = "Array")
            private List<ArticleRawBlockInlineStyleRange> inlineStyleRanges;
            @ApiModelProperty(value = "entityRanges", dataType = "ArticleRawBlockEntityRange")
            private ArticleRawBlockEntityRange entityRanges;
            @ApiModelProperty(value = "data", dataType = "ArticleRawBlockData")
            private ArticleRawBlockData data;


            @Data
            @ApiModel(value = "ArticleRawBlockInlineStyleRange", description = "")
            public static class ArticleRawBlockInlineStyleRange {

                @ApiModelProperty(value = "offset", dataType = "Integer", example = "2")
                private Integer offset;
                @ApiModelProperty(value = "length", dataType = "Integer", example = "2")
                private Integer length;
                @ApiModelProperty(value = "style", dataType = "String", example = "FONTSIZE-16")
                private String style;

            }

            @Data
            @ApiModel(value = "ArticleRawBlockEntityRange", description = "")
            public static class ArticleRawBlockEntityRange {

                @ApiModelProperty(value = "offset", dataType = "Integer", example = "2")
                private Integer offset;
                @ApiModelProperty(value = "length", dataType = "Integer", example = "2")
                private Integer length;
                @ApiModelProperty(value = "key", dataType = "Integer", example = "2")
                private Integer key;

            }

            @Data
            @ApiModel(value = "ArticleRawBlockData", description = "")
            public static class ArticleRawBlockData {

                @ApiModelProperty(value = "alignment", dataType = "String", example = "center")
                private String alignment;
                @ApiModelProperty(value = "float", dataType = "String", example = "center")
                @JSONField(name = "float")
                private String float_;
                @ApiModelProperty(value = "nodeAttributes", dataType = "nodeAttributes")
                private ArticleRawBlockDataNodeAttributes nodeAttributes;


                @Data
                @ApiModel(value = "ArticleRawBlockDataNodeAttributes", description = "")
                public static class ArticleRawBlockDataNodeAttributes {

                    @ApiModelProperty(value = "src", dataType = "String", example = "https://zhengwen.aminer.cn/31xF5I0UxVnCI")
                    private String src;
                    @ApiModelProperty(value = "class", dataType = "String", example = "rich_pages wxw-img")
                    @JSONField(name = "class")
                    private String class_;


                }

            }

        }

    }


}
