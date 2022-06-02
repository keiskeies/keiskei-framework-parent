package top.keiskeiframework.common.base.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/16 14:48
 */
@Data
public class BaseEntity implements IBaseEntity<String> {

    private static final long serialVersionUID = 1051825228141800634L;
    @ApiModelProperty(value = "ID", dataType = "String")
    @MongoId(targetType = FieldType.OBJECT_ID)
    protected String id = ObjectId.get().toString();

    protected Boolean d;
}
