package top.keiskeiframework.info.tag.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.TreeEntity;

/**
 * <p>
 * 标签
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 22:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("tag")
@ApiModel(value = "TagInfo", description = "标签")
public class TagInfo extends TreeEntity {

    @ApiModelProperty(value = "标签名称", dataType = "String", example = "雷军")
    private String name;
}
