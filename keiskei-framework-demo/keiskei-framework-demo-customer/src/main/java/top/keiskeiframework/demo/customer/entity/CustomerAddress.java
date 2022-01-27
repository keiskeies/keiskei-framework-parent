package top.keiskeiframework.demo.customer.entity;

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
 * @since 2022/1/25 18:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_customer_address")
@ApiModel(value = "CustomerAddress", description = "收货地址")
public class CustomerAddress extends ListEntity<Long> {

    @ApiModelProperty(value = "用户", dataType = "Long")
    private Long customerId;

    @ApiModelProperty(value = "姓名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "手机号", dataType = "String")
    private String phone;

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

    @ApiModelProperty(value = "详细地址", dataType = "String")
    private String address;

    @ApiModelProperty(value = "默认地址", dataType = "Boolean")
    private Boolean byDefault;
}
