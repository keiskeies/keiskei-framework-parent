package top.keiskeiframework.common.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.injector.dto.ManyToManyResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 接口
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 14:54
 */
public interface BaseEntityMapper<T extends ListEntity> extends BaseMapper<T> {

    List<ManyToManyResult> findManyToMany(T t);
    List<Map<Long, Long>> findOneToMany(T t);
    List<Map<Long, Long>> findManyToOne(T t);
    List<Map<Long, Long>> findOneToOne(T t);
}
