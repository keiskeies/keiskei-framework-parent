package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;

import java.io.Serializable;

/**
 * <p>
 * 普通数据service
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/25 22:44
 */

public class CommonServiceImpl<T extends ListEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends AbstractBaseServiceImpl<T, ID, M>
        implements BaseService<T, ID>, IService<T> {

}
