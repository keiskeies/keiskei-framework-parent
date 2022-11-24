package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import top.keiskeiframework.cloud.feign.front.service.IListFrontService;
import top.keiskeiframework.common.base.entity.IListEntity;

import java.io.Serializable;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class ListFrontServiceImpl<DTO extends IListEntity<ID>, T extends IListEntity<ID>, ID extends Serializable>
        extends AbstractBaseFrontServiceImpl<DTO, T, ID>
        implements IListFrontService<DTO, ID> {

}
