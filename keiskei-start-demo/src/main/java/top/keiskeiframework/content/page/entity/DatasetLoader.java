package top.keiskeiframework.content.page.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.BaseEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 21:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("pwc_loaders")
@ApiModel(value = "DatasetLoader", description = "DatasetLoader")
public class DatasetLoader extends BaseEntity {

    @ApiModelProperty(value = "数据集ID", dataType = "String", example = "10001")
    private String datasetId;
    @ApiModelProperty(value = "URL", dataType = "String", example = "https://paperswithcode.com/paper/gru-ode-bayes-continuous-modeling-of")
    private String url;
    @ApiModelProperty(value = "标题", dataType = "String", example = "GRU-ODE-Bayes: Continuous modeling of sporadically-observed time series")
    private String title;
    @ApiModelProperty(value = "星级", dataType = "String", example = "7779")
    private String star;
}
