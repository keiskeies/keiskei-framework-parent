package top.keiskeiframework.cloud.feign.front.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public R<Page<T>> list(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show,
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "20", required = false) Integer size,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "asc", required = false) String asc
    ) {
        return R.ok(listFrontService.page(
                conditions,
                show,
                page,
                size,
                desc,
                asc
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
            @RequestParam(name = "asc", required = false) String asc
    ) {
        return R.ok(listFrontService.options(
                conditions,
                show,
                page,
                size,
                desc,
                asc
        ));
    }


    @GetMapping("/all")
    @ApiOperation("全部下拉框")
    public R<List<T>> all(
            @RequestParam(name = "conditions", required = false) String conditions,
            @RequestParam(name = "show", required = false) String show
    ) {
        return R.ok(listFrontService.all(
                conditions,
                show
        ));
    }
}