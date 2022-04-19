package top.keiskeiframework.common.base.controller.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.vo.R;

import java.util.List;

/**
 * <p>
 * 通用前端控制器
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class ListControllerImpl<T extends ListEntity>
        extends AbstractControllerServiceImpl<T>
        implements IControllerService<T> {

    @Autowired
    private ListServiceImpl<T> listService;


    @GetMapping
    @ApiOperation("列表")
    public R<Page<T>> page(BaseRequestVO<T> request, BasePageVO<T> page, @RequestParam(required = false, defaultValue = "false") Boolean complete) {
        Page<T> tPage;
        if (complete) {
            tPage = listService.pageComplete(request, page);
        } else {
            tPage = listService.page(request, page);
        }
        return R.ok(tPage);
    }


    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestVO<T> request, @RequestParam(required = false, defaultValue = "false") Boolean complete) {
        List<T> ts;
        if (complete) {
            ts = listService.findAllComplete(request);
        } else {
            ts = listService.findAll(request);
        }
        return R.ok(ts);
    }

}
