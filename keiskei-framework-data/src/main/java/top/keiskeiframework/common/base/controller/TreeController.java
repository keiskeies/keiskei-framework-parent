package top.keiskeiframework.common.base.controller;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.common.vo.R;

import java.util.List;

/**
 * <p>
 * 树形数据通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class TreeController<T extends TreeEntity> {

    @Autowired
    private TreeServiceImpl<T> baseService;


    @GetMapping(value = {"", "/options"})
    @ApiOperation("列表")
    public R<List<T>> tree(
            @ApiParam(value = "查询条件", type="JSONSting", example = "{\"column\":\"id\", \"condition\": \"EQ|IN|GE|GT|LIKE|LL|LR|BT\", \"value\": [1001,1002] }")
            String conditions
    ) {
        BaseRequest<T> baseRequest = new BaseRequest<>();
        baseRequest.setConditions(conditions);
        List<T> list = baseService.findAll(baseRequest);
        return R.ok(new TreeEntityUtils<>(list).getTree(null));
    }

    @GetMapping("/{id}")
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable String id) {
        return R.ok(baseService.getById(id));
    }

    @PostMapping
    @ApiOperation("新增")
    public R<T> save(@RequestBody T fieldInfo) {
        return R.ok(baseService.saveAndNotify(fieldInfo));
    }

    @PutMapping
    @ApiOperation("更新")
    public R<T> update(@RequestBody T fieldInfo) {
        return R.ok(baseService.updateAndNotify(fieldInfo));
    }

    @PutMapping("/sort")
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortDTO baseSortDto) {
        baseService.changeSort(baseSortDto);
        return R.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable String id) {
        baseService.deleteByIdAndNotify(id);
        return R.ok(Boolean.TRUE);
    }
}