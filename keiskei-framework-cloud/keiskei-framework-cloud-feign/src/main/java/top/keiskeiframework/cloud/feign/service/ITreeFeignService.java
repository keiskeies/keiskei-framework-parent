package top.keiskeiframework.cloud.feign.service;

import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.cloud.feign.vo.PageRequestVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ITreeEntity;
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
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/21 22:16
 */
public interface ITreeFeignService<T extends ITreeEntity<ID>, ID extends Serializable> {

    @GetMapping
    R<PageResultVO<T>> page(@SpringQueryMap PageRequestVO requestVO);

    @GetMapping("/options")
    R<List<T>> options(@SpringQueryMap BaseRequestVO baseRequestVO);


    /**
     * 条件查询单个
     *
     * @param conditions 查询条件
     * @return 。
     */
    @GetMapping("/conditions")
    R<T> getOne(@RequestParam(name = "conditions", required = false) String conditions);

    /**
     * 查询数量
     *
     * @param conditions 查询条件
     * @return count
     */
    @GetMapping("/count")
    R<Long> count(@RequestParam(name = "conditions", required = false) String conditions);


    /**
     * 判断是否存在
     *
     * @param conditions 查询条件
     * @return 。
     */
    @GetMapping("/exist")
    R<Boolean> exist(@RequestParam(name = "conditions", required = false) String conditions);

    /**
     * 详情
     *
     * @param id ID
     * @return data
     */
    @GetMapping("/{id}")
    R<T> findById(@PathVariable("id") ID id);


    /**
     * 条件详情
     *
     * @param column 字段
     * @param value  value
     * @return data
     */
    @GetMapping("/column")
    R<T> getOne(@RequestParam(name = "column") String column, @RequestParam(name = "value") Serializable value);

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
     * 删除
     *
     * @param id ID
     * @return boolean
     */
    @DeleteMapping("/{id}")
    R<Boolean> delete(@PathVariable("id") ID id);


    /**
     * 通过字段删除
     *
     * @param column 字段
     * @param value  值
     * @return 。
     */
    @DeleteMapping("/column")
    R<Boolean> delete(@RequestParam(name = "column") String column, @RequestParam(name = "value") Serializable value);


    /**
     * 删除多个
     *
     * @param ids ids
     * @return boolean
     */
    @DeleteMapping("/multi")
    R<Boolean> delete(@RequestBody List<ID> ids);


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
     * @param sumColumn  求和字段
     * @param conditions 查询条件
     * @return 。
     */
    @GetMapping("/statistic")
    R<Map<String, Double>> statistic(
            @RequestParam(name = "column") String column,
            @RequestParam(name = "columnType") ColumnType columnType,
            @RequestParam(name = "timeField", required = false) String timeField,
            @RequestParam(name = "timeDelta") TimeDeltaEnum timeDelta,
            @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end,
            @RequestParam(name = "calcType", required = false) CalcType calcType,
            @RequestParam(name = "sumColumn", required = false) String sumColumn,
            @RequestParam(name = "conditions", required = false) String conditions
    );


}
