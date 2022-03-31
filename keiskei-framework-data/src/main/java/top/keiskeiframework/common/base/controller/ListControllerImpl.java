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
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class ListControllerImpl<T extends ListEntity<ID>, ID extends Serializable> implements IControllerService<T, ID> {

    @Autowired
    private ListServiceImpl<T, ID> listService;


    @Override
    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page) {
        return R.ok(listService.page(request, page));
    }


    @Override
    @ApiOperation("数量")
    public R<Long> count(BaseRequestVO<T, ID> request) {
        return R.ok(listService.count(request));
    }

    @Override
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page) {
        return R.ok(listService.findAll(request, page));
    }
    @Override
    @ApiOperation("全部下拉框")
    public R<List<T>> all(BaseRequestVO<T, ID> request) {
        return R.ok(listService.findAll(request));
    }

    @Override
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(listService.findById(id));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class}) T t) {
        return R.ok(listService.saveAndNotify(t));
    }

    @Override
    @ApiOperation("新增")
    public R<List<T>> save(@RequestBody @Validated({Insert.class}) List<T> ts) {
        return R.ok(listService.saveAll(ts));
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        return R.ok(listService.updateAndNotify(fieldInfo));
    }

    @Override
    @ApiOperation("更新")
    public R<List<T>> update(@RequestBody @Validated({Update.class}) List<T> ts) {
        return R.ok(listService.updateAll(ts));
    }

    @Override
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortVO<ID> baseSortVO) {
        listService.changeSort(baseSortVO);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        listService.deleteByIdAndNotify(id);
        return R.ok(Boolean.TRUE);
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody List<ID> ids) {
        listService.deleteByIds(ids);
        return R.ok(Boolean.TRUE);
    }
}
