package top.keiskeiframework.common.base.jpa.service.impl;

import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.service.IBaseService;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/14 23:52
 */
public class JpaBaseServiceImpl<T extends IBaseEntity<ID>, ID extends Serializable> extends AbstractJpaServiceImpl<T, ID> implements IBaseService<T, ID> {
}
