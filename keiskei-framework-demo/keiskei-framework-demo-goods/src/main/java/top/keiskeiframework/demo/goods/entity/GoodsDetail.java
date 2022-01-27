package top.keiskeiframework.demo.goods.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.keiskeiframework.common.base.entity.ListEntity;

import javax.persistence.*;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 17:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_goods_detail")
@ApiModel(value = "GoodsDetail", description = "商品详情")
public class GoodsDetail extends ListEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", referencedColumnName = "id")
    @ApiModelProperty(value = "商品", dataType = "Goods")
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_classify_detail_id", referencedColumnName = "id")
    @ApiModelProperty(value = "商品细分", dataType = "GoodsClassifyDetail")
    private GoodsClassifyDetail goodsClassifyDetail;

    @ApiModelProperty(value = "库存", dataType = "Integer")
    private Integer stock;
}
