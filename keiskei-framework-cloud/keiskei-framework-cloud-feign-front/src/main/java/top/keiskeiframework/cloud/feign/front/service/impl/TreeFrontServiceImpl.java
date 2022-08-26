package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.cloud.feign.dto.PageResultDTO;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.cloud.feign.enums.CalcType;
import top.keiskeiframework.cloud.feign.enums.ColumnType;
import top.keiskeiframework.cloud.feign.front.service.ITreeFrontService;
import top.keiskeiframework.cloud.feign.front.util.TreeEntityDtoUtils;
import top.keiskeiframework.cloud.feign.service.ITreeFeignService;
import top.keiskeiframework.cloud.feign.vo.BasePageVO;
import top.keiskeiframework.cloud.feign.vo.BaseRequestVO;
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
public class TreeFrontServiceImpl<T extends TreeEntityDTO<T, ID>, ID extends Serializable> implements ITreeFrontService<T, ID> {

    @Autowired
    protected ITreeFeignService<T, ID> treeFeignService;

    @Override
    public PageResultDTO<T> page(BaseRequestVO requestVO, BasePageVO pageVO) {
        PageResultDTO<T> tiPage = treeFeignService.page(
                requestVO.getConditions(),
                requestVO.getShow(),
                pageVO.getOffset(),
                pageVO.getPage(),
                pageVO.getSize(),
                requestVO.getDesc(),
                requestVO.getAsc(),
                false
        ).getData();

        if (requestVO.getTree()) {
            List<T> treeList = new TreeEntityDtoUtils<>(tiPage.getData()).getTreeAll();
            tiPage.setData(treeList);
        }
        return tiPage;
    }


    @Override
    public List<T> options(BaseRequestVO requestVO) {
        List<T> noTreeData = treeFeignService.options(
                requestVO.getConditions(),
                requestVO.getShow(),
                requestVO.getDesc(),
                requestVO.getAsc(),
                false
        ).getData();
        if (requestVO.getTree()) {
            return new TreeEntityDtoUtils<>(noTreeData).getTreeAll();
        }
        return noTreeData;
    }

    @Override
    public T findById(ID id) {
        return treeFeignService.findById(id).getData();
    }


    @Override
    public T getOne(String conditions) {
        return treeFeignService.getOne(conditions).getData();
    }

    @Override
    public Long count(String conditions) {
        return treeFeignService.count(conditions).getData();
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
    public void deleteById(ID id) {
        treeFeignService.delete(id);
    }

    @Override
    public Boolean delete(List<ID> ids) {
        return treeFeignService.delete(ids).getData();
    }

    @Override
    public Map<String, Double> statistic(
            String column,
            ColumnType columnType,
            String timeField,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String sumColumn,
            String conditions
    ) {
        return treeFeignService.statistic(
                column,
                columnType,
                timeField,
                timeDelta,
                start,
                end,
                calcType,
                sumColumn,
                conditions
        ).getData();
    }
}
