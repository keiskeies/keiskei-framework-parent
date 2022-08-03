package top.keiskeiframework.cloud.feign.front.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.dto.PageResultDTO;
import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.cloud.feign.front.service.IFrontService;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.MdcUtils;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 前置控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/3 21:34
 */
public abstract class AbstractFrontControllerServiceImpl<T extends ListEntityDTO<ID>, ID extends Serializable> implements IFrontControllerService<T, ID> {

    @Autowired
    private IFrontService<T, ID> frontService;

    @GetMapping
    @ApiOperation("列表")
    public R<PageResultDTO<T>> list(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(name = "offset", required = false) Long offset,
            @RequestParam(name = "page", defaultValue = "1", required = false) Long page,
            @RequestParam(name = "size", defaultValue = "20", required = false) Long size,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "asc", required = false) String asc,
            @RequestParam(name = "complete", defaultValue = "false", required = false) Boolean complete,
            @RequestParam(name = "tree", defaultValue = "true", required = false) Boolean tree

    ) {
        return R.ok(frontService.page(
                conditions,
                show,
                offset,
                page,
                size,
                desc,
                asc,
                complete,
                tree
        ));
    }

    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "asc", required = false) String asc,
            @RequestParam(name = "complete", defaultValue = "false", required = false) Boolean complete,
            @RequestParam(name = "tree", defaultValue = "true", required = false) Boolean tree
    ) {
        return R.ok(frontService.options(
                conditions,
                show,
                desc,
                asc,
                complete,
                tree));
    }

    @Override
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(frontService.findById(id));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class}) T t) {
        return R.ok(frontService.save(t));
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T t) {
        T oldT = frontService.findById(t.getId());
        if (null == oldT || !MdcUtils.getIntegerUserId().equals(oldT.getCreateUserId())) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        return R.ok(frontService.update(t));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        T oldT = frontService.findById(id);
        if (null == oldT) {
            return R.ok(true);
        }
        if (!MdcUtils.getIntegerUserId().equals(oldT.getCreateUserId())) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        frontService.deleteById(id);
        return R.ok(Boolean.TRUE);
    }
}
