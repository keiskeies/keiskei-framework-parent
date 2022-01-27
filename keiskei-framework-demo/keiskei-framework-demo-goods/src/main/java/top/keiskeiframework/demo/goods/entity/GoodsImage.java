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
 * @since 2022/1/25 17:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_goods_image")
@ApiModel(value = "GoodsImage", description = "商品图片")
public class GoodsImage extends ListEntity<Long> {

    @ApiModelProperty(value = "URL", dataType = "String")
    private String url;

    @ApiModelProperty(value = "宽度", dataType = "Integer")
    private Integer width;

    @ApiModelProperty(value = "高度", dataType = "Integer")
    private Integer height;

    @ApiModelProperty(value = "大小", dataType = "Long")
    private Long size;
}
