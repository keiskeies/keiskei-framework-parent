package top.keiskeiframework.common.base.controller.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用前端控制器
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
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
    public R<Page<T>> page(
            BaseRequestVO<T, ID> request,
            BasePageVO page, @RequestParam(required = false,
            defaultValue = "false") Boolean complete) {
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
    public R<List<T>> options(
            BaseRequestVO<T, ID> request,
            @RequestParam(required = false, defaultValue = "false") Boolean complete) {
        List<T> ts;
        if (complete) {
            ts = listService.listComplete(request);
        } else {
            ts = listService.list(request);
        }
        return R.ok(ts);
    }


}
