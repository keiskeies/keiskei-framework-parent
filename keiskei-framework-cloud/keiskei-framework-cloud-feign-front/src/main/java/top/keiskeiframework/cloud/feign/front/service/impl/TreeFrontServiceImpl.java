package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.cloud.feign.enums.CalcType;
import top.keiskeiframework.cloud.feign.enums.ColumnType;
import top.keiskeiframework.cloud.feign.front.service.ITreeFrontService;
import top.keiskeiframework.cloud.feign.front.util.TreeEntityDtoUtils;
import top.keiskeiframework.cloud.feign.service.ITreeFeignService;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class TreeFrontServiceImpl<T extends TreeEntityDTO> implements ITreeFrontService<T> {

    @Autowired
    protected ITreeFeignService<T> treeFeignService;

    @Override
    public Page<T> page(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc,
            Boolean tree
    ) {
        Page<T> noTreeResult = treeFeignService.page(
                conditions,
                show,
                page,
                size,
                desc,
                asc,
                false
        ).getData();

        if (tree) {
            List<T> treeList = new TreeEntityDtoUtils<>(noTreeResult.getContent()).getTreeAll();
            return new PageImpl<>(treeList, noTreeResult.getPageable(), noTreeResult.getTotalElements());
        } else {
            return new PageImpl<>(noTreeResult.getContent(), noTreeResult.getPageable(), noTreeResult.getTotalElements());
        }
    }

    @Override
    public List<T> all(
            String conditions,
            String show,
            Long id,
            Boolean tree
    ) {
        List<T> noTreeData = treeFeignService.all(
                conditions,
                show,
                id,
                false
        ).getData();
        if (tree) {
            return new TreeEntityDtoUtils<>(noTreeData).getTreeAll(id);
        }
        {
            return noTreeData;
        }
    }

    @Override
    public List<T> options(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc,
            Long id,
            Boolean tree
    ) {
        List<T> noTreeData = treeFeignService.options(
                conditions,
                show,
                page,
                size,
                desc,
                asc,
                id,
                false
        ).getData();
        if (tree) {
            return new TreeEntityDtoUtils<>(noTreeData).getTreeAll(id);
        }
        {
            return noTreeData;
        }
    }

    @Override
    public T findById(Long id) {
        return treeFeignService.getOne(id).getData();
    }

    @Override
    public T findByColumn(String column, Serializable value) {
        return treeFeignService.getOne(column, value).getData();
    }

    @Override
    public T save(T t) {
        return treeFeignService.save(t).getData();
    }

    @Override
    public List<T> save(List<T> ts) {
        return treeFeignService.save(ts).getData();
    }

    @Override
    public T update(T t) {
        return treeFeignService.update(t).getData();
    }

    @Override
    public List<T> update(List<T> ts) {
        return treeFeignService.update(ts).getData();
    }

    @Override
    public void deleteById(Long id) {
        treeFeignService.delete(id);
    }

    @Override
    public Boolean delete(List<Long> ids) {
        return treeFeignService.delete(ids).getData();
    }

    @Override
    public Map<String, Double> statistic(
            String column,
            String timeField,
            ColumnType columnType,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String conditions
    ) {
        return treeFeignService.statistic(
                column,
                timeField,
                columnType,
                timeDelta,
                start,
                end,
                calcType,
                conditions
        ).getData();
    }
}
