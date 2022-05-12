package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.keiskeiframework.common.base.entity.MiddleEntity;
import top.keiskeiframework.common.base.service.IMiddleService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 19:56
 */
public abstract class AbstractMiddleServiceImpl
        <T extends MiddleEntity<ID1, ID2>, ID1 extends Serializable, ID2 extends Serializable, M extends BaseMapper<T>>
        extends AbstractServiceImpl<T, String, M>
        implements IMiddleService<T, ID1, ID2> {

    @Autowired
    private IMiddleService<T, ID1, ID2> middleService;

    @Override
    public List<T> getById1(Serializable id1) {
        return middleService.listByColumn("id1", id1);
    }

    @Override
    public List<T> getById2(Serializable id2) {
        return middleService.listByColumn("id2", id2);
    }

    @Override
    @Transactional
    public List<T> saveOrUpdateById1(List<T> ts) {
        middleService.removeById1(ts.get(0).getId1());
        middleService.saveBatch(ts);
        return ts;
    }

    @Override
    @Transactional
    public List<T> saveOrUpdateById2(List<T> ts) {
        middleService.removeById2(ts.get(0).getId2());
        middleService.saveBatch(ts);
        return ts;
    }


    @Override
    public Boolean removeById1(ID1 id1) {
        return middleService.removeByColumn("id1", id1);
    }

    @Override
    public Boolean removeById2(ID2 id2) {
        return middleService.removeByColumn("id2", id2);
    }
}
