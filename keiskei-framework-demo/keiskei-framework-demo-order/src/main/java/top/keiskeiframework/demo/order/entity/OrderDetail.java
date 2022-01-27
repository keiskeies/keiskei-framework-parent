package top.keiskeiframework.demo.order.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.util.data.MoneyDeserializer;
import top.keiskeiframework.common.util.data.MoneySerializer;

import javax.persistence.*;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 23:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_order_detail")
@ApiModel(value = "OrderDetail", description = "订单详情")
public class OrderDetail extends ListEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ApiModelProperty(value = "关联订单", dataType = "Order")
    private Order order;

    @ApiModelProperty(value = "商品ID", dataType = "Long")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称", dataType = "String")
    private String goodsName;

    @ApiModelProperty(value = "商品图片", dataType = "String")
    private String goodsImage;

    @ApiModelProperty(value = "商品数量", dataType = "Integer")
    private Integer goodsCount;

    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    @ApiModelProperty(value = "商品单价", dataType = "Double")
    private Long goodsUnitPrice;

    @JsonDeserialize(converter = MoneyDeserializer.class)
    @JsonSerialize(converter = MoneySerializer.class)
    @ApiModelProperty(value = "合计", dataType = "Double")
    private Long goodsPrice;

}
