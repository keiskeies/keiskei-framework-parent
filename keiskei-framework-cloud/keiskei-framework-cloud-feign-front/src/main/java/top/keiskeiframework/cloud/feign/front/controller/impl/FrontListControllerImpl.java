package top.keiskeiframework.cloud.feign.front.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import top.keiskeiframework.cloud.feign.dto.BasePageDTO;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.cloud.feign.front.service.impl.ListFrontServiceImpl;
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
public class FrontListControllerImpl<T extends ListEntityDTO<ID>, ID extends Serializable>
        extends AbstractFrontControllerServiceImpl<T, ID>
        implements IFrontControllerService<T, ID> {

    @Autowired
    protected ListFrontServiceImpl<T, ID> listFrontService;

    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(BaseRequestDTO<T, ID> request, BasePageDTO<T, ID> page) {
        return R.ok(listFrontService.page(request, page));
    }


    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestDTO<T, ID> request, BasePageDTO<T, ID> page) {
        return R.ok(listFrontService.options(request, page));
    }


    @GetMapping("/all")
    @ApiOperation("全部下拉框")
    public R<List<T>> all(BaseRequestDTO<T, ID> request) {
        return R.ok(listFrontService.all(request));
    }
}
