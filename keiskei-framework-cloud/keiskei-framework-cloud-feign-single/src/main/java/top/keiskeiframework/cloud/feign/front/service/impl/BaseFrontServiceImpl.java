package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import top.keiskeiframework.cloud.feign.front.service.IBaseFrontService;
import top.keiskeiframework.common.base.entity.IBaseEntity;

import java.io.Serializable;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class BaseFrontServiceImpl<DTO extends IBaseEntity<ID>, T extends IBaseEntity<ID>, ID extends Serializable>
        extends AbstractBaseFrontServiceImpl<DTO, T, ID>
        implements IBaseFrontService<DTO, ID> {

}
