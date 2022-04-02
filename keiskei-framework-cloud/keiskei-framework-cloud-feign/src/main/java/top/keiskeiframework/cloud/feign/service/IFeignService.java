package top.keiskeiframework.cloud.feign.service;

import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.BaseSortDTO;
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
public interface IFeignService<T extends ListEntityDTO<ID>, ID extends Serializable> {

    /**
     * 查询数量
     *
     * @param request request
     * @return count
     */
    @GetMapping("/count")
    R<Long> count(BaseRequestDTO<T, ID> request);

    /**
     * 下拉框
     *
     * @param request request
     * @return AllData
     */
    @GetMapping("/all")
    R<List<T>> all(BaseRequestDTO<T, ID> request);

    /**
     * 详情
     *
     * @param id ID
     * @return data
     */
    @GetMapping("/{id}")
    R<T> getOne(@PathVariable("id") ID id);


    /**
     * 条件详情
     * @param column 字段
     * @param value value
     * @return data
     */
    @GetMapping("/{column}/{value}")
    R<T> getOne(@PathVariable("column") String column, @PathVariable("value") Serializable value);

    /**
     * 保存
     *
     * @param t t
     * @return t with id
     */
    @PostMapping
    R<T> save(@RequestBody T t);

    /**
     * 保存多个
     *
     * @param ts ts
     * @return dataList
     */
    @PostMapping("/multi")
    R<List<T>> save(@RequestBody List<T> ts);

    /**
     * 更新
     *
     * @param t t
     * @return t
     */
    @PutMapping
    R<T> update(@RequestBody T t);


    /**
     * 更新多个
     *
     * @param ts ts
     * @return dataList
     */
    @PutMapping("/multi")
    R<List<T>> update(@RequestBody List<T> ts);

    /**
     * 修改排序
     *
     * @param baseSortVO dto
     * @return boolean
     */
    @PatchMapping("/sort")
    R<Boolean> changeSort(@RequestBody BaseSortDTO<ID> baseSortVO);

    /**
     * 删除
     *
     * @param id ID
     * @return boolean
     */
    @DeleteMapping("/{id}")
    R<Boolean> delete(@PathVariable("id") ID id);

    /**
     * 删除多个
     *
     * @param ids ids
     * @return boolean
     */
    @DeleteMapping("/multi")
    R<Boolean> delete(@RequestBody List<ID> ids);


}
