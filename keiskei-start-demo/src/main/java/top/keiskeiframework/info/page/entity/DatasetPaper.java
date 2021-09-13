package top.keiskeiframework.info.page.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.BaseEntity;

import java.util.List;

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
@Document("pwc_papers")
@ApiModel(value = "DatasetPaper", description = "DatasetPaper")
public class DatasetPaper extends BaseEntity {

    @ApiModelProperty(value = "数据集ID", dataType = "String", example = "10001")
    private String datasetId;
    @ApiModelProperty(value = "task", dataType = "String", example = "Multivariate Time Series Forecasting")
    private String task;
    @ApiModelProperty(value = "URL", dataType = "String", example = "https://paperswithcode.com/paper/gru-ode-bayes-continuous-modeling-of")
    private String url;
    @ApiModelProperty(value = "标题", dataType = "String", example = "GRU-ODE-Bayes: Continuous modeling of sporadically-observed time series")
    private String title;
    @ApiModelProperty(value = "作者", dataType = "String", example = "Edward De Brouwer,Jaak Simm,Adam Arany,Yves Moreau")
    private String authors;
    @ApiModelProperty(value = "简介", dataType = "String", example = "Modeling real-world multidimensional time series can be particularly challenging when these are sporadically observed ")
    private String info;
    @ApiModelProperty(value = "PDF URL", dataType = "String", example = "https://arxiv.org/pdf/1905.12374v2.pd")
    private String pdfUrl;
    @ApiModelProperty(value = "codeUrlList", dataType = "Array[String]", example = "[\"https://github.com/MLforHealth/MIMIC_Extract\",\"https://github.com/asjad99/MIMIC_RL_COACH\"]")
    private List<String> codeUrlList;
}
