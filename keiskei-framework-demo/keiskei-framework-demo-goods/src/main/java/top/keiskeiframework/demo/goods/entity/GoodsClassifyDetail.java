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
 * @since 2022/1/25 17:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_goods_classify_detail")
@ApiModel(value = "GoodsClassifyDetail", description = "商品细分详情")
public class GoodsClassifyDetail extends ListEntity<Long> {

    @ApiModelProperty(value = "名称", dataType = "String")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", referencedColumnName = "id")
    @ApiModelProperty(value = "商品", dataType = "Goods")
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_classify_id", referencedColumnName = "id")
    @ApiModelProperty(value = "商品细分", dataType = "GoodsClassify")
    private GoodsClassify goodsClassify;
}
