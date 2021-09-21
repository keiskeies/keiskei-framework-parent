package top.keiskeiframework.email.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import top.keiskeiframework.common.base.entity.BaseEntity;

/**
 * <p>
 * 推送模版
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/21 18:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Document("push_template")
@ApiModel(value = "PushTemplate", description = "推送模版")
public class PushTemplate extends BaseEntity {

    @ApiModelProperty(value = "模版名称", dataType = "String")
    private String name;

    @ApiModelProperty(value = "模版内容", dataType = "String")
    private String content;
}
