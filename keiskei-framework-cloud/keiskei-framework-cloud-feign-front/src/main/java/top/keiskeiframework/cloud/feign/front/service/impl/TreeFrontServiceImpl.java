package top.keiskeiframework.cloud.feign.front.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.cloud.feign.dto.BasePageDTO;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.cloud.feign.front.service.ITreeFrontService;
import top.keiskeiframework.cloud.feign.service.ITreeFeignService;

import java.io.Serializable;
import java.util.List;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class TreeFrontServiceImpl<T extends TreeEntityDTO<ID>, ID extends Serializable> implements ITreeFrontService<T, ID> {

    @Autowired
    protected ITreeFeignService<T, ID> treeFeignService;

    @Override
    public Page<T> page(BaseRequestDTO<T, ID> request, BasePageDTO<T, ID> page, Boolean tree) {
        return treeFeignService.list(
                (!CollectionUtils.isEmpty(request.getConditions()) ? JSON.toJSONString(request.getConditions()) : null),
                (!CollectionUtils.isEmpty(request.getShow()) ? String.join(",", request.getShow()) : null),
                page.getPage(),
                page.getSize(),
                page.getDesc(),
                page.getAsc(),
                tree
        ).getData();
    }

    @Override
    public List<T> all(BaseRequestDTO<T, ID> request, ID id, Boolean tree) {
        return treeFeignService.all(
                (!CollectionUtils.isEmpty(request.getConditions()) ? JSON.toJSONString(request.getConditions()) : null),
                (!CollectionUtils.isEmpty(request.getShow()) ? String.join(",", request.getShow()) : null),
                id,
                tree
        ).getData();
    }

    @Override
    public List<T> options(BaseRequestDTO<T, ID> request, BasePageDTO<T, ID> page, ID id, Boolean tree) {
        return treeFeignService.options(
                (!CollectionUtils.isEmpty(request.getConditions()) ? JSON.toJSONString(request.getConditions()) : null),
                (!CollectionUtils.isEmpty(request.getShow()) ? String.join(",", request.getShow()) : null),
                page.getPage(),
                page.getSize(),
                page.getDesc(),
                page.getAsc(),
                id,
                tree
        ).getData();
    }

    @Override
    public T findById(ID id) {
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
    public T update(T t) {
        return treeFeignService.update(t).getData();
    }


    @Override
    public void deleteById(ID id) {
        treeFeignService.delete(id);
    }
}
