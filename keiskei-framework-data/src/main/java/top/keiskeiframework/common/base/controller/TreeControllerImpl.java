package top.keiskeiframework.common.base.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
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
    @ApiOperation("列表")
    public R<Page<T>> list(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page) {
        return R.ok(treeService.page(request, page));
    }

    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(
            BaseRequestVO<T, ID> request,
            BasePageVO<T, ID> page,
            @RequestParam(required = false, defaultValue = "true") Boolean tree
    ) {
        Page<T> pageList = treeService.page(request, page);
        if (tree) {
            List<T> treeList = new TreeEntityUtils<>(pageList.getContent()).getTreeAll();
            return R.ok(new TreePageImpl<>(treeList, pageList.getPageable(), pageList.getTotalElements()));
        } else {
            return R.ok(pageList);
        }
    }

    @Override
    @ApiOperation("数量")
    public R<Long> count(BaseRequestVO<T, ID> request) {
        return R.ok(treeService.count(request));
    }

    @Override
    public R<List<T>> options(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page) {
        return R.ok(treeService.findAll(request, page));
    }

    @Override
    public R<List<T>> all(BaseRequestVO<T, ID> request) {
        return R.ok(treeService.findAll(request));
    }

    @ApiOperation("下拉框")
    public R<List<T>> options(
            BaseRequestVO<T, ID> request,
            BasePageVO<T, ID> page,
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
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(treeService.findById(id));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody T fieldInfo) {
        return R.ok(treeService.saveAndNotify(fieldInfo));
    }

    @Override
    @ApiOperation("新增")
    public R<List<T>> save(@RequestBody @Validated({Insert.class}) List<T> ts) {
        return R.ok(treeService.saveAll(ts));
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        return R.ok(treeService.updateAndNotify(fieldInfo));
    }

    @Override
    @ApiOperation("更新")
    public R<List<T>> update(@RequestBody @Validated({Update.class}) List<T> ts) {
        return R.ok(treeService.updateAll(ts));
    }

    @Override
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortVO<ID> baseSortVO) {
        treeService.changeSort(baseSortVO);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        treeService.deleteByIdAndNotify(id);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody List<ID> ids) {
        treeService.deleteByIds(ids);
        return R.ok(Boolean.TRUE);
    }
}
