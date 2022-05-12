package top.keiskeiframework.common.base.controller.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.ListBaseService;
import top.keiskeiframework.common.vo.R;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
    private ListBaseService<T, ID> listBaseService;
    private final static String TRUE = "true";
    private final static String COMPLETE = "complete";

    @Override
    public R<T> getOne(ID id) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String complete = request.getParameter(COMPLETE);
        if (StringUtils.isEmpty(complete) || TRUE.equals(complete)) {
            return R.ok(listBaseService.getByIdComplete(id));
        } else {
            return R.ok(listBaseService.getById(id));
        }
    }

    @Override
    public R<Page<T>> page(BaseRequestVO<T, ID> baseRequestVO, BasePageVO page) {
        Page<T> tPage;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String complete = request.getParameter(COMPLETE);
        if (StringUtils.isEmpty(complete) || TRUE.equals(complete)) {
            tPage = listBaseService.pageComplete(baseRequestVO, page);
        } else {
            tPage = listBaseService.page(baseRequestVO, page);
        }
        return R.ok(tPage);
    }


    @Override
    public R<List<T>> options(BaseRequestVO<T, ID> baseRequest) {
        List<T> ts;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String complete = request.getParameter(COMPLETE);
        if (!StringUtils.isEmpty(complete) && TRUE.equals(complete)) {
            ts = listBaseService.listComplete(baseRequest);
        } else {
            ts = listBaseService.list(baseRequest);
        }
        return R.ok(ts);
    }


}
