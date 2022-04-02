package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.cloud.feign.front.service.FeignBaseService;

import java.io.Serializable;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class FeignTreeServiceImpl<T extends TreeEntityDTO<ID>, ID extends Serializable> extends AbstractFeignServiceImpl<T, ID> implements FeignBaseService<T, ID> {



}
