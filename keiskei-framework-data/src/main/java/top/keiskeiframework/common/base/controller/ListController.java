package top.keiskeiframework.common.base.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
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
public class ListController<T extends BaseEntity> {

    @Autowired
    private ListServiceImpl<T> baseService;


    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(BaseRequest<T> request) {
        return R.ok(baseService.page(request));
    }

    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> list(
            @ApiParam(value = "查询条件", type = "JSONSting", example = "{\"column\":\"id\", \"condition\": \"EQ|IN|GE|GT|LIKE|LL|LR|BT\", \"value\": [1001,1002] }")
            @RequestParam(required = false)
            String conditions,
            @ApiParam(value = "查询字段", type = "String", example = "id, name")
            @RequestParam(required = false)
            String show
    ) {
        BaseRequest<T> baseRequest = new BaseRequest<>();
        baseRequest.setConditions(conditions);
        baseRequest.setShow(show);
        return R.ok(baseService.findAll());
    }

    @GetMapping("/{id}")
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable String id) {
        return R.ok(baseService.findById(id));
    }

    @PostMapping
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class}) T fieldInfo) {
        return R.ok(baseService.saveAndNotify(fieldInfo));
    }

    @PutMapping
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
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
