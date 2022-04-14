package top.keiskeiframework.common.base.controller.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public R<IPage<T>> page(BaseRequestVO<T> request, BasePageVO<T> page) {
        IPage<T> tPage = listService.page(request, page);
        return R.ok(tPage);
    }


    @GetMapping("/options")
    @ApiOperation("下拉框")
    public R<List<T>> options(BaseRequestVO<T> request) {
        return R.ok(listService.findAll(request));
    }

}
