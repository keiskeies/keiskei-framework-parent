package top.keiskeiframework.common.base.service.impl;

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
public class BaseServiceImpl<T extends IBaseEntity<ID>, ID extends Serializable> extends AbstractServiceImpl<T, ID> implements IBaseService<T, ID> {
}
