package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/25 22:44
 */

public class CommonServiceImpl<T extends ListEntity<ID>, ID extends Serializable>
        extends AbstractBaseServiceImpl<T, ID>
        implements BaseService<T, ID>, IService<T> {

}
