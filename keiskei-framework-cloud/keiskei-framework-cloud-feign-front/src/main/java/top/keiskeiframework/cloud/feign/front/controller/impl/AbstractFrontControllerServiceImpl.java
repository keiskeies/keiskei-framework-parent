package top.keiskeiframework.cloud.feign.front.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.cloud.feign.front.service.IFrontService;
import top.keiskeiframework.common.annotation.validate.Insert;
import top.keiskeiframework.common.annotation.validate.Update;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.MdcUtils;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;

/**
 * <p>
 * 前置控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/3 21:34
 */
public abstract class AbstractFrontControllerServiceImpl<T extends ListEntityDTO> implements IFrontControllerService<T> {

    @Autowired
    private IFrontService<T> frontService;


    @Override
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable Long id) {
        return R.ok(frontService.findById(id));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody @Validated({Insert.class})T fieldInfo) {
        return R.ok(frontService.save(fieldInfo));
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        T oldT = frontService.findById(fieldInfo.getId());
        if (null == oldT || !MdcUtils.getLongUserId().equals(oldT.getCreateUserId())) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        return R.ok(frontService.update(fieldInfo));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable Long id) {
        T oldT = frontService.findById(id);
        if (null == oldT) {
            return R.ok(true);
        }
        if (!MdcUtils.getLongUserId().equals(oldT.getCreateUserId())) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        frontService.deleteById(id);
        return R.ok(Boolean.TRUE);
    }
}
