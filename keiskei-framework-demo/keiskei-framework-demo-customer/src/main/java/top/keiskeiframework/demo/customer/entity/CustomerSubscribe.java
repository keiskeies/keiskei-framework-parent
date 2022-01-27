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
 * @since 2022/1/25 18:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_customer_subscribe")
@ApiModel(value = "CustomerSubscribe", description = "关注订阅")
public class CustomerSubscribe extends ListEntity<Long> {

    @ApiModelProperty(value = "关注人", dataType = "Long")
    private Long customerId;

    @ApiModelProperty(value = "被关注人", dataType = "Long")
    private Long subscribeId;
}
