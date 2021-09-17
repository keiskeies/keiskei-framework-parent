package top.keiskeiframework.content.page.entity;

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
 * Benchmark
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 20:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("pwc_benchmarks")
@ApiModel(value = "DatasetBenchmark", description = "DatasetBenchmark")
public class DatasetBenchmark extends BaseEntity {

    @ApiModelProperty(value = "数据集ID", dataType = "String", example = "10001")
    private String datasetId;
    @ApiModelProperty(value = "task", dataType = "String", example = "Multivariate Time Series Forecasting")
    private String task;
    @ApiModelProperty(value = "datasetVariant", dataType = "String", example = "MIMIC-III")
    private String datasetVariant;
    @ApiModelProperty(value = "模型", dataType = "String", example = "GRU-ODE-Bayes")
    private String bestModel;
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
