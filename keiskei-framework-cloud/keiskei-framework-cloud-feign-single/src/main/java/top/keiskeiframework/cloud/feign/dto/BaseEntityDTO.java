package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;
import top.keiskeiframework.common.base.entity.IBaseEntity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 17:01
 */
@Data
public class BaseEntityDTO<ID extends Serializable> implements IBaseEntity<ID> {

    private static final long serialVersionUID = -531829598114928379L;

    protected ID id;
}
