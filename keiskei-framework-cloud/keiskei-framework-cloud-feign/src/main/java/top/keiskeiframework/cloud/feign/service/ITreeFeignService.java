package top.keiskeiframework.cloud.feign.service;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.cloud.feign.dto.BaseSortDTO;
import top.keiskeiframework.cloud.feign.dto.ChartRequestDTO;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * controller通用接口
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 22:16
 */
public interface ITreeFeignService<T extends TreeEntityDTO<ID>, ID extends Serializable> {

    /**
     * 分页查询
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param page       页码
     * @param size       size
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param tree       是否树状结构
     * @return 。
     */
    @GetMapping
    R<Page<T>> list(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "20", required = false) Integer size,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "asc", required = false) String asc,
            @RequestParam(name = "tree", required = false, defaultValue = "true") Boolean tree
    );

    /**
     * 下拉框
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param page       页码
     * @param size       size
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param id         根节点ID
     * @param tree       是否树状结构
     * @return 。
     */
    @GetMapping("/options")
    R<List<T>> options(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "20", required = false) Integer size,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "asc", required = false) String asc,
            @RequestParam(name = "id", required = false) ID id,
            @RequestParam(name = "tree", required = false, defaultValue = "true") Boolean tree
    );

    /**
     * 全部下拉框
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param id         根节点ID
     * @param tree       是否树状结构
     * @return 。
     */
    @GetMapping("/all")
    R<List<T>> all(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(required = false) ID id,
            @RequestParam(required = false, defaultValue = "true") Boolean tree);

    /**
     * 查询数量
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @return count
     */
    @GetMapping("/count")
    R<Long> count(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show
    );

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
     *
     * @param column 字段
     * @param value  value
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


    /**
     * 数据统计
     *
     * @param chartRequestDTO 统计条件
     * @return 。
     */
    @GetMapping("/statistic")
    R<Map<String, Double>> statistic(ChartRequestDTO chartRequestDTO);


}
