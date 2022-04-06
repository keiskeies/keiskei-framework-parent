package top.keiskeiframework.common.base.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.common.util.TreeEntityUtils;
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
public class TreeControllerImpl<T extends TreeEntity<ID>, ID extends Serializable>
        extends AbstractControllerServiceImpl<T, ID>
        implements IControllerService<T, ID> {

    @Autowired
    private TreeServiceImpl<T, ID> treeService;


    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> page(
            BaseRequestVO<T, ID> request,
            BasePageVO<T, ID> page,
            @RequestParam(required = false, defaultValue = "true") Boolean tree
    ) {
        Page<T> pageList = treeService.page(request, page);
        if (tree) {
            List<T> treeList = new TreeEntityUtils<>(pageList.getContent()).getTreeAll();
            return R.ok(new PageImpl<>(treeList, pageList.getPageable(), pageList.getTotalElements()));
        } else {
            return R.ok(new PageImpl<>(pageList.getContent(), pageList.getPageable(), pageList.getTotalElements()));
        }
    }


    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(
            BaseRequestVO<T, ID> request,
            BasePageVO<T, ID> page,
            @RequestParam(required = false) ID id,
            @RequestParam(required = false, defaultValue = "true") Boolean tree) {
        List<T> list;
        if (tree) {
            list = treeService.findAll(request);
            return R.ok(new TreeEntityUtils<>(list).getTreeAll(id));
        } else {
            list = treeService.findAll(request, page);
            return R.ok(list);
        }
    }


    @GetMapping("/all")
    @ApiOperation("全部下拉框")
    public R<List<T>> all(
            BaseRequestVO<T, ID> request,
            @RequestParam(required = false) ID id,
            @RequestParam(required = false, defaultValue = "true") Boolean tree) {
        List<T> list = treeService.findAll(request);
        if (tree) {
            return R.ok(new TreeEntityUtils<>(list).getTreeAll(id));
        } else {
            return R.ok(list);
        }
    }
}
