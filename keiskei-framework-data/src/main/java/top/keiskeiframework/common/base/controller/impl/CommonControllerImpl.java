package top.keiskeiframework.common.base.controller.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.impl.CommonServiceImpl;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 树形数据通用前端控制器
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class CommonControllerImpl<T extends ListEntity<ID>, ID extends Serializable>
        extends AbstractControllerServiceImpl<T, ID>
        implements IControllerService<T, ID> {

    @Autowired
    private CommonServiceImpl<T, ID> commonService;


    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        return R.ok(commonService.page(request, page));
    }

    @DeleteMapping
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody @Validated T t) {
        return R.ok(commonService.removeById(t));
    }


    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestVO<T, ID> request) {
        return R.ok(commonService.list(request));
    }
}
