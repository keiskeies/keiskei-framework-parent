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
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 16:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_goods")
@ApiModel(value = "Goods", description = "商品")
public class Goods extends ListEntity<Long> {

    @ApiModelProperty(value = "名称", dataType = "String")
    private String name;

    @ApiModelProperty(value = "商家", dataType = "Merchant")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Merchant merchant;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "goods_id")
    @ApiModelProperty(value = "图片", dataType = "List<GoodsImage>")
    private List<GoodsImage> images;

    @ApiModelProperty(value = "视频", dataType = "String")
    private String video;

    @ManyToMany(targetEntity = GoodsClassify.class, cascade = CascadeType.DETACH)
    @JoinTable(name = "demo_goods_goods_classify",
            joinColumns = {@JoinColumn(name = "goods_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "good_classify_id", referencedColumnName = "id")})
    @ApiModelProperty(value = "商品细分", dataType = "List<GoodsClassify>")
    private List<GoodsClassify> goodsClassifies;

}
