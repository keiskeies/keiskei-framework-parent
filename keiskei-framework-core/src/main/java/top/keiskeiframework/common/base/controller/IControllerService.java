package top.keiskeiframework.common.base.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * controller通用接口
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 22:16
 */
public interface IControllerService<T extends IBaseEntity<ID>, ID extends Serializable> {

    /**
     * 列表
     *
     * @param BaseRequestVO 查询条件
     * @param page          分页条件
     * @return 。
     */
    @GetMapping
    R<PageResultVO<T>> page(BaseRequestVO<T, ID> BaseRequestVO, BasePageVO page);

    /**
     * 下拉框
     *
     * @param BaseRequestVO 查询条件
     * @return 。
     */
    @GetMapping("/options")
    R<List<T>> options(BaseRequestVO<T, ID> BaseRequestVO);

    /**
     * 详情
     *
     * @param id ID
     * @return data
     */
    @GetMapping("/{id}")
    R<T> getOne(@PathVariable("id") ID id);

    /**
     * 条件查询
     *
     * @param request request
     * @return .
     */
    @GetMapping("/conditions")
    R<T> getOne(BaseRequestVO<T, ID> request);

    /**
     * 判断是否存在
     *
     * @param request request
     * @return boolean
     */
    @GetMapping("/exist")
    R<Boolean> exist(BaseRequestVO<T, ID> request);

    /**
     * 查询数量
     *
     * @param request request
     * @return count
     */
    @GetMapping("/count")
    R<Long> count(BaseRequestVO<T, ID> request);

    /**
     * 保存
     *
     * @param t t
     * @return t with id
     */
    @PostMapping
    R<T> save(@RequestBody @Validated(Insert.class) T t);

    /**
     * 保存多个
     *
     * @param ts ts
     * @return dataList
     */
    @PostMapping("/multi")
    R<List<T>> save(@RequestBody @Validated(Insert.class) List<T> ts);

    /**
     * 更新
     *
     * @param t t
     * @return t
     */
    @PutMapping
    R<T> update(@RequestBody @Validated(Update.class) T t);

    /**
     * 更新多个
     *
     * @param ts ts
     * @return dataList
     */
    @PutMapping("/multi")
    R<List<T>> update(@RequestBody @Validated(Update.class) List<T> ts);

    /**
     * 删除
     *
     * @param id ID
     * @return boolean
     */
    @DeleteMapping("/{id}")
    R<Boolean> deleteById(@PathVariable("id") ID id);

    /**
     * 删除
     *
     * @param t t
     * @return .l
     */
    @DeleteMapping
    R<Boolean> delete(@RequestBody T t);

    /**
     * 删除多个
     *
     * @param ids ids
     * @return boolean
     */
    @DeleteMapping("/multi")
    R<Boolean> deleteMulti(@RequestBody List<ID> ids);


    /**
     * 条件删除
     *
     * @param conditions 条件
     * @return 。
     */
    @DeleteMapping("/conditions")
    R<Boolean> deleteByConditions(@RequestBody List<QueryConditionVO> conditions);

    /**
     * 数据统计
     *
     * @param column     统计字段
     * @param timeField  时间字段
     * @param columnType 字段类型
     * @param timeDelta  时间间隔
     * @param start      起始时间
     * @param end        结束时间
     * @param calcType   计算方式
     * @param sumColumn  求字和段
     * @param conditions 查询条件
     * @return 。
     */
    @GetMapping("/statistic")
    R<Map<String, Double>> statistic(
            @RequestParam(name = "column") String column,
            @RequestParam(name = "columnType") ColumnType columnType,
            @RequestParam(name = "timeField", required = false) String timeField,
            @RequestParam(name = "timeDelta", required = false) TimeDeltaEnum timeDelta,
            @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end,
            @RequestParam(name = "calcType", required = false) CalcType calcType,
            @RequestParam(name = "sumColumn", required = false) String sumColumn,
            @RequestParam(name = "conditions", required = false) String conditions
    );


}
