package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.service.ITreeBaseService;

import java.io.Serializable;

/**
 * 基础查询接口
 *
 * @param <T> .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class TreeServiceImpl<T extends TreeEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends AbstractBaseServiceImpl<T, ID, M>
        implements ITreeBaseService<T, ID>, IBaseService<T, ID>, IService<T> {


}
