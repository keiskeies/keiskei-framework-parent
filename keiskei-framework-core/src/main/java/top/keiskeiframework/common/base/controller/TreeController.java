package top.keiskeiframework.common.base.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.TreeService;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.common.vo.BaseSortDto;
import top.keiskeiframework.common.vo.R;

import java.util.List;

/**
 * <p>
 * 通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class TreeController<T extends TreeEntity> {

    @Autowired
    private TreeService<T> treeService;


    @GetMapping(value = {"","/options"})
    @ApiOperation("列表")
    public R<List<T>> tree() {
        List<T> list = treeService.options();
        return R.ok(new TreeEntityUtils<>(list).getTree(null));
    }

    @GetMapping("/{id:-?[\\d]+}")
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable Long id) {
        return R.ok(treeService.getById(id));
    }

    @PostMapping
    @ApiOperation("新增")
    public R<T> save(@RequestBody T fieldInfo) {
        return R.ok(treeService.save(fieldInfo));
    }

    @PutMapping
    @ApiOperation("更新")
    public R<T> update(@RequestBody T fieldInfo) {
        return R.ok(treeService.update(fieldInfo));
    }

    @PutMapping("/sort")
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody  @Validated BaseSortDto baseSortDto) {
        treeService.changeSort(baseSortDto);
        return R.ok(Boolean.TRUE);
    }
    @DeleteMapping("/{id:-?[\\d]+}")
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable Long id) {
        treeService.deleteById(id);
        return R.ok(Boolean.TRUE);
    }
}
