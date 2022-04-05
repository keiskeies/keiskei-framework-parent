package top.keiskeiframework.cloud.feign.front.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.keiskeiframework.cloud.feign.dto.BasePageDTO;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.cloud.feign.front.service.impl.TreeFrontServiceImpl;
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
public class FrontTreeControllerImpl<T extends TreeEntityDTO<ID>, ID extends Serializable>
        extends AbstractFrontControllerServiceImpl<T, ID>
        implements IFrontControllerService<T, ID> {

    @Autowired
    protected TreeFrontServiceImpl<T, ID> feignTreeService;

    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> list(
            BaseRequestDTO<T, ID> request,
            BasePageDTO<T, ID> page,
            @RequestParam(required = false, defaultValue = "true") Boolean tree
    ) {
        return R.ok(feignTreeService.page(request, page, tree));
    }

    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(
            BaseRequestDTO<T, ID> request,
            BasePageDTO<T, ID> page,
            @RequestParam(required = false) ID id,
            @RequestParam(required = false, defaultValue = "true") Boolean tree) {
        return R.ok(feignTreeService.options(request, page, id, tree));
    }


    @GetMapping("/all")
    @ApiOperation("全部下拉框")
    public R<List<T>> all(
            BaseRequestDTO<T, ID> request,
            @RequestParam(required = false) ID id,
            @RequestParam(required = false, defaultValue = "true") Boolean tree
    ) {
        return R.ok(feignTreeService.all(request, id, tree));
    }

}
