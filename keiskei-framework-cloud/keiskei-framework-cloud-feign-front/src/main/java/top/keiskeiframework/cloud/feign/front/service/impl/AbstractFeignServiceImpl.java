package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.front.service.FeignBaseService;
import top.keiskeiframework.cloud.feign.service.IFeignService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 基础服务实现
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/21 11:46
 */
@Slf4j
public abstract class AbstractFeignServiceImpl<T extends ListEntityDTO<ID>, ID extends Serializable> implements FeignBaseService<T, ID> {

    @Autowired
    protected IFeignService<T, ID> feignService;



    @Override
    public List<T> findAll(BaseRequestDTO<T, ID> request) {
        return feignService.all(request).getData();
    }



    @Override
    public T findById(ID id) {
        return feignService.getOne(id).getData();
    }


    @Override
    public T findByColumn(String column, Serializable value) {
        return feignService.getOne(column, value).getData();
    }

    @Override
    public T save(T t) {
        return feignService.save(t).getData();
    }



    @Override
    public T update(T t) {
        return feignService.update(t).getData();
    }


    @Override
    public void deleteById(ID id) {
        feignService.delete(id);
    }



}
