package top.keiskeiframework.info.page.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import top.keiskeiframework.common.base.entity.BaseEntity;

/**
 * <p>
 * 数据集
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 20:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("pwc_dataset")
@ApiModel(value = "Dataset", description = "数据集")
public class Dataset extends BaseEntity {

    @ApiModelProperty(value = "数据集URL", dataType = "String", example = "https://paperswithcode.com/dataset/market-1501")
    private String url;
    @ApiModelProperty(value = "标题", dataType = "String", example = "Market-1501")
    private String title;
    @ApiModelProperty(value = "摘要", dataType = "String", example = "Introduced by Zheng et al. in Scalable Person Re-Identification: A Benchmark")
    @Field("abstract")
    private String abstract_;
    @ApiModelProperty(value = "简介", dataType = "String", example = "Market-1501 is a large-scale public benchmark dataset for person re-identification. ")
    private String info;
    @ApiModelProperty(value = "主页", dataType = "String", example = "https://www.kaggle.com/pengcw1/market-1501/data")
    private String homePage;
    @ApiModelProperty(value = "数据集id（paper，loader,benchmark通过这个id与数据集关联）", dataType = "String", example = "https://www.kaggle.com/pengcw1/market-1501/data")
    private String datasetId;
}
