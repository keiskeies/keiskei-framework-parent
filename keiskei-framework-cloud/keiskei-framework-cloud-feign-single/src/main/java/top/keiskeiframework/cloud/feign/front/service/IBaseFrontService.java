package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.common.base.entity.IBaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 基础服务接口
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface IBaseFrontService<T extends IBaseEntity<ID>, ID extends Serializable> extends IFrontService<T, ID> {


}