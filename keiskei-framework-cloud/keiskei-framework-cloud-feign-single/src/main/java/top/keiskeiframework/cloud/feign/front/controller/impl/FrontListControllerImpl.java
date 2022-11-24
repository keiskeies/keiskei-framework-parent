package top.keiskeiframework.cloud.feign.front.controller.impl;

import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.common.base.entity.IListEntity;

import java.io.Serializable;

/**
 * <p>
 * 通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class FrontListControllerImpl<T extends IListEntity<ID>, ID extends Serializable>
        extends AbstractFrontControllerServiceImpl<T, ID>
        implements IFrontControllerService<T, ID> {
}
