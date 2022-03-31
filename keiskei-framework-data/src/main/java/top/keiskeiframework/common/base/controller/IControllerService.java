package top.keiskeiframework.common.base.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
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
public interface IControllerService<T extends ListEntity<ID>, ID extends Serializable> {

    /**
     * 分页列表
     *
     * @param request reqeust
     * @param page    page
     * @return list
     */
    R<Page<T>> list(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page);

    /**
     * 查询数量
     *
     * @param request request
     * @return count
     */
    @GetMapping("/count")
    R<Long> count(BaseRequestVO<T, ID> request);

    /**
     * 下拉框
     *
     * @param request request
     * @param page    page
     * @return AllData
     */
    @GetMapping("/options")
    R<List<T>> options(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page);

    /**
     * 全部下拉框
     *
     * @param request request
     * @return AllData
     */
    @GetMapping("/all")
    R<List<T>> all(BaseRequestVO<T, ID> request);

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
    R<Boolean> changeSort(@RequestBody BaseSortVO<ID> baseSortVO);

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
