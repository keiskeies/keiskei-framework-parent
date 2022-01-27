package top.keiskeiframework.common.base.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.dto.BasePageDto;
import top.keiskeiframework.common.base.dto.BaseRequestDto;
import top.keiskeiframework.common.base.dto.BaseSortDTO;
import top.keiskeiframework.common.base.dto.TreePageImpl;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 树形数据通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class TreeControllerImpl<T extends TreeEntity<ID>, ID extends Serializable> implements IControllerService<T, ID> {

    @Autowired
    private TreeServiceImpl<T, ID> treeService;

    @Override
    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(BaseRequestDto<T, ID> request, BasePageDto<T, ID> page) {
        return R.ok(treeService.page(request, page));
    }

    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> treeList(
            BaseRequestDto<T, ID> request,
            BasePageDto<T, ID> page,
            @RequestParam(required = false, defaultValue = "true") Boolean tree) {
        Page<T> pageList = treeService.page(request, page);
        if (tree) {
            List<T> treeList = new TreeEntityUtils<>(pageList.getContent()).getTreeAll();
            return R.ok(new TreePageImpl<>(treeList, pageList.getPageable(), pageList.getTotalElements()));
        } else {
            return R.ok(pageList);
        }
    }

    @Override
    @GetMapping("/count")
    @ApiOperation("数量")
    public R<Long> count(BaseRequestDto<T, ID> request) {
        return R.ok(treeService.count(request));
    }

    @Override
    public R<List<T>> options(BaseRequestDto<T, ID> request, BasePageDto<T, ID> page) {
        return R.ok(treeService.findAll(request, page));
    }

    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(
            BaseRequestDto<T, ID> request,
            BasePageDto<T, ID> page,
            @RequestParam(required = false) ID id,
            @RequestParam(required = false, defaultValue = "true") Boolean tree) {
        List<T> list;
        if (request.requestEmpty()) {
            list = treeService.findAll();
        } else {
            list = treeService.findAll(request, page);
        }
        if (tree) {
            return R.ok(new TreeEntityUtils<>(list).getTreeAll(id));
        } else {
            return R.ok(list);
        }
    }

    @Override
    @GetMapping("/{id}")
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(treeService.findById(id));
    }

    @Override
    @PostMapping
    @ApiOperation("新增")
    public R<T> save(@RequestBody T fieldInfo) {
        return R.ok(treeService.saveAndNotify(fieldInfo));
    }

    @Override
    @PostMapping("/multi")
    @ApiOperation("新增")
    public R<List<T>> save(@RequestBody @Validated({Insert.class}) List<T> ts) {
        return R.ok(treeService.saveAll(ts));
    }

    @Override
    @PutMapping
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        return R.ok(treeService.updateAndNotify(fieldInfo));
    }

    @Override
    @PutMapping("/multi")
    @ApiOperation("更新")
    public R<List<T>> update(@RequestBody @Validated({Update.class}) List<T> ts) {
        return R.ok(treeService.updateAll(ts));
    }

    @Override
    @PatchMapping("/sort")
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortDTO<ID> baseSortDto) {
        treeService.changeSort(baseSortDto);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        treeService.deleteByIdAndNotify(id);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @DeleteMapping("/multi")
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody List<ID> ids) {
        treeService.deleteByIds(ids);
        return R.ok(Boolean.TRUE);
    }
}
