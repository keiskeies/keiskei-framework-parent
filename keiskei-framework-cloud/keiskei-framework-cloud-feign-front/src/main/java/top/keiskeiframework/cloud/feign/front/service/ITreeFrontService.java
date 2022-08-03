package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;

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
public interface ITreeFrontService<T extends TreeEntityDTO<T, ID>, ID extends Serializable> extends IFrontService<T,
        ID> {


}
