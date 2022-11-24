package top.keiskeiframework.cloud.feign.front.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.cloud.feign.front.service.IFrontService;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
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
public abstract class AbstractFrontControllerServiceImpl<T extends IBaseEntity<ID>, ID extends Serializable> implements IFrontControllerService<T, ID> {

    @Autowired
    private IFrontService<T, ID> frontService;

    @Override
    @ApiOperation("列表")
    public R<PageResultVO<T>> page(BaseRequestVO<T, ID> requestVO, BasePageVO pageVO) {
        return R.ok(frontService.page(requestVO, pageVO));
    }

    @Override
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestVO<T, ID> requestVO) {
        return R.ok(frontService.options(requestVO));
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
        if (null == oldT) {
            throw new BizException(BizExceptionEnum.NOT_FOUND_ERROR);
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
        frontService.deleteById(id);
        return R.ok(Boolean.TRUE);
    }
}
