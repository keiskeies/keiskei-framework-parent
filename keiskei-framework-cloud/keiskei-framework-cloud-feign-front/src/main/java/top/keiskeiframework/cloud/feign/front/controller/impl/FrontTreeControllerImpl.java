package top.keiskeiframework.cloud.feign.front.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.keiskeiframework.cloud.feign.dto.PageResultDTO;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;
import top.keiskeiframework.cloud.feign.front.controller.IFrontControllerService;
import top.keiskeiframework.cloud.feign.front.service.impl.TreeFrontServiceImpl;
import top.keiskeiframework.common.vo.R;

import java.util.List;

/**
 * <p>
 * 树形数据通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class FrontTreeControllerImpl<T extends TreeEntityDTO>
        extends AbstractFrontControllerServiceImpl<T>
        implements IFrontControllerService<T> {

    @Autowired
    protected TreeFrontServiceImpl<T> feignTreeService;

    @GetMapping
    @ApiOperation("列表")
    public R<PageResultDTO<T>> list(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "20", required = false) Integer size,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "asc", required = false) String asc,
            @RequestParam(required = false, defaultValue = "true") Boolean tree
    ) {
        return R.ok(feignTreeService.page(
                conditions,
                show,
                page,
                size,
                desc,
                asc,
                tree
        ));
    }

    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "20", required = false) Integer size,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "asc", required = false) String asc,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "tree", required = false, defaultValue = "true") Boolean tree) {
        return R.ok(feignTreeService.options(
                conditions,
                show,
                page,
                size,
                desc,
                asc,
                id,
                tree));
    }


    @GetMapping("/all")
    @ApiOperation("全部下拉框")
    public R<List<T>> all(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false, defaultValue = "true") Boolean tree
    ) {
        return R.ok(feignTreeService.all(
                conditions,
                show,
                id,
                tree
        ));
    }

}
