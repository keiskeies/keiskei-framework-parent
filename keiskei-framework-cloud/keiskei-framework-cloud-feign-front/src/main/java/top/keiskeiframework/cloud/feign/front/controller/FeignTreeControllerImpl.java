package top.keiskeiframework.cloud.feign.front.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.cloud.feign.front.service.impl.FeignTreeServiceImpl;
import top.keiskeiframework.common.annotation.validate.Update;
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
public class FeignTreeControllerImpl<T extends TreeEntityDTO<ID>, ID extends Serializable> implements IFeignControllerService<T, ID> {

    @Autowired
    protected FeignTreeServiceImpl<T, ID> feignTreeService;

    @Override
    public R<List<T>> all(BaseRequestDTO<T, ID> request) {
        return R.ok(feignTreeService.findAll(request));
    }

    @Override
    @ApiOperation("详情")
    public R<T> getOne(@PathVariable ID id) {
        return R.ok(feignTreeService.findById(id));
    }

    @Override
    @ApiOperation("新增")
    public R<T> save(@RequestBody T fieldInfo) {
        return R.ok(feignTreeService.save(fieldInfo));
    }

    @Override
    @ApiOperation("更新")
    public R<T> update(@RequestBody @Validated({Update.class}) T fieldInfo) {
        return R.ok(feignTreeService.update(fieldInfo));
    }

    @Override
    @ApiOperation("删除")
    public R<Boolean> delete(@PathVariable ID id) {
        feignTreeService.deleteById(id);
        return R.ok(Boolean.TRUE);
    }
}
