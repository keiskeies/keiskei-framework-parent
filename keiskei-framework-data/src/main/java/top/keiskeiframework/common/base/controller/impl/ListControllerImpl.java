package top.keiskeiframework.common.base.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
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
public class ListControllerImpl<T extends ListEntity<ID>, ID extends Serializable>
        extends AbstractControllerServiceImpl<T, ID>
        implements IControllerService<T, ID> {

    @Autowired
    private ListServiceImpl<T, ID> listService;


    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page) {
        Page<T> tPage = listService.page(request, page);
        return R.ok(tPage);
    }


    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page) {
        return R.ok(listService.findAll(request, page));
    }


    @GetMapping("/all")
    @ApiOperation("全部下拉框")
    public R<List<T>> all(BaseRequestVO<T, ID> request) {
        return R.ok(listService.findAll(request));
    }
}
