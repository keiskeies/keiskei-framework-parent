package top.keiskeiframework.common.base.controller.impl;

import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.entity.IBaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 树形数据通用前端控制器
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class BaseControllerImpl<T extends IBaseEntity<ID>, ID extends Serializable>
        extends AbstractControllerServiceImpl<T, ID>
        implements IControllerService<T, ID> {

}