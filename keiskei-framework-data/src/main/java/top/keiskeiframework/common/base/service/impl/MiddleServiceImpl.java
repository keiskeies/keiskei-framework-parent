package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.keiskeiframework.common.base.entity.MiddleEntity;
import top.keiskeiframework.common.base.service.IMiddleService;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 20:06
 */
public class MiddleServiceImpl
        <T extends MiddleEntity<ID1, ID2>, ID1 extends Serializable, ID2 extends Serializable, M extends BaseMapper<T>>
        extends AbstractMiddleServiceImpl<T, ID1, ID2, M>
        implements IMiddleService<T, ID1, ID2> {

}
