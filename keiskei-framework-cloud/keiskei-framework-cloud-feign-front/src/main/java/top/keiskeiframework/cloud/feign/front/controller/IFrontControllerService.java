package top.keiskeiframework.cloud.feign.front.controller;

import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * controller通用接口
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 22:16
 */
public interface IFrontControllerService<T extends ListEntityDTO<ID>, ID extends Serializable> {


    /**
     * 详情
     *
     * @param id ID
     * @return data
     */
    @GetMapping("/{id}")
    R<T> getOne(@PathVariable("id") ID id);

    /**
     * 保存
     *
     * @param t t
     * @return t with id
     */
    @PostMapping
    R<T> save(@RequestBody T t);

    /**
     * 更新
     *
     * @param t t
     * @return t
     */
    @PutMapping
    R<T> update(@RequestBody T t);


    /**
     * 删除
     *
     * @param id ID
     * @return boolean
     */
    @DeleteMapping("/{id}")
    R<Boolean> delete(@PathVariable("id") ID id);


}
