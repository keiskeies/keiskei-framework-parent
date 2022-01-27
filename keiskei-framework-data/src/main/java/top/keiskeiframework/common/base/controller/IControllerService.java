package top.keiskeiframework.common.base.controller;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.dto.BasePageDto;
import top.keiskeiframework.common.base.dto.BaseRequestDto;
import top.keiskeiframework.common.base.dto.BaseSortDTO;
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
    @GetMapping
    R<Page<T>> list(BaseRequestDto<T, ID> request, BasePageDto<T, ID> page);

    /**
     * 查询数量
     *
     * @param request request
     * @return count
     */
    @GetMapping("/count")
    R<Long> count(BaseRequestDto<T, ID> request);

    /**
     * 下拉框
     *
     * @param request request
     * @param page    page
     * @return AllData
     */
    @GetMapping("/options")
    R<List<T>> options(BaseRequestDto<T, ID> request, BasePageDto<T, ID> page);

    /**
     * 详情
     *
     * @param id ID
     * @return data
     */
    @GetMapping("/{id}")
    R<T> getOne(@PathVariable ID id);

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
     * @param baseSortDto dto
     * @return boolean
     */
    @PatchMapping("/sort")
    R<Boolean> changeSort(@RequestBody BaseSortDTO<ID> baseSortDto);

    /**
     * 删除
     *
     * @param id ID
     * @return boolean
     */
    @DeleteMapping("/{id}")
    R<Boolean> delete(@PathVariable ID id);

    /**
     * 删除多个
     *
     * @param ids ids
     * @return boolean
     */
    @DeleteMapping("/multi")
    R<Boolean> delete(@RequestBody List<ID> ids);


}
