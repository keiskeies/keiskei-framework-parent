package top.keiskeiframework.common.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.injector.dto.ManyToManyResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 接口
 * </p>
 * @param <T> .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 14:54
 */
public interface BaseEntityMapper<T extends ListEntity<ID>, ID extends Serializable> extends BaseMapper<T> {

    List<ManyToManyResult> findManyToMany(T t);
    void saveManyToMany(T t);
    void deleteManyToMany(@Param("id") ID id);
}
