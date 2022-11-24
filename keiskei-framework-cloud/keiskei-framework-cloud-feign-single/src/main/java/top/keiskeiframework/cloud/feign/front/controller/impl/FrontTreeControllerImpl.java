package top.keiskeiframework.cloud.feign.front.controller.impl;

import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.common.base.entity.ITreeEntity;

import java.io.Serializable;

/**
 * <p>
 * 树形数据通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class FrontTreeControllerImpl<T extends ITreeEntity<ID>, ID extends Serializable>
        extends AbstractFrontControllerServiceImpl<T, ID>
        implements IFrontControllerService<T, ID> {

}
