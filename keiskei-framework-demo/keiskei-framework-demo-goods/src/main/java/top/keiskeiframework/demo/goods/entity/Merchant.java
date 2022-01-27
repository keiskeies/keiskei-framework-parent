package top.keiskeiframework.demo.goods.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 16:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_merchant")
@ApiModel(value = "Merchant", description = "商家")
public class Merchant extends ListEntity<Long> {

    @ApiModelProperty(value = "名称", dataType = "String")
    private String name;

    @ApiModelProperty(value = "头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "粉丝数量", dataType = "Integer")
    private Integer fansNumber;

    @ApiModelProperty(value = "国家", dataType = "Long")
    private Long countryId;
    private String countryName;

    @ApiModelProperty(value = "省", dataType = "Long")
    private Long provinceId;
    private String provinceName;

    @ApiModelProperty(value = "市", dataType = "Long")
    private Long cityId;
    private String cityName;

    @ApiModelProperty(value = "区/县", dataType = "Long")
    private Long prefectureId;
    private String prefectureName;
}
