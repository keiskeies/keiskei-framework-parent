package top.keiskeiframework.common.base.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
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
public class ListController<T extends ListEntity<ID>, ID extends Serializable> {

    @Autowired
    private ListServiceImpl<T, ID> listService;


    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(BaseRequest<T, ID> request) {
        return R.ok(listService.page(request));
    }


    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> list() {
        return R.ok(listService.options());
    }

    @GetMapping("/{id}")
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(listService.findById(id));
    }

    @PostMapping
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class}) T fieldInfo) {
        return R.ok(listService.saveAndNotify(fieldInfo));
    }

    @PutMapping
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        return R.ok(listService.updateAndNotify(fieldInfo));
    }

    @PutMapping("/sort")
    @ApiOperation("更改排序")
    public R<Boolean> changeSort(@RequestBody @Validated BaseSortDTO baseSortDto) {
        listService.changeSort(baseSortDto);
        return R.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        listService.deleteByIdAndNotify(id);
        return R.ok(Boolean.TRUE);
    }
}
