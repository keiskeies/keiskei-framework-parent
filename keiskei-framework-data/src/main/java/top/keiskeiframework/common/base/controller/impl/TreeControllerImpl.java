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
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.TreeBaseService;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.common.vo.R;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 树形数据通用前端控制器
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 13:02
 */
public class TreeControllerImpl<T extends TreeEntity<ID>, ID extends Serializable>
        extends AbstractControllerServiceImpl<T, ID>
        implements IControllerService<T, ID> {

    @Autowired
    private TreeBaseService<T, ID> treeBaseService;
    private final static String TRUE = "true";
    private final static String TREE = "tree";
    private final static String ID = "id";


    @Override
    public R<Page<T>> page(BaseRequestVO<T, ID> baseRequestVO, BasePageVO page) {
        List<T> list = treeBaseService.list(baseRequestVO);
        Page<T> tPage = new Page<>(1, list.size(), list.size());
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String complete = request.getParameter(TREE);
        if (StringUtils.isEmpty(complete) || TRUE.equals(complete)) {
            String id = request.getParameter(ID);
            ID typeId = null;
            if (!StringUtils.isEmpty(id)) {
                typeId = (ID) id;
            }
            tPage.setRecords(new TreeEntityUtils<>(list).getTreeAll(typeId));
        } else {
            tPage.setRecords(list);
        }
        return R.ok(tPage);
    }

    @Override
    public R<List<T>> options(BaseRequestVO<T, ID> baseRequest) {
        List<T> list = treeBaseService.list(baseRequest);
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String complete = request.getParameter(TREE);
        if (StringUtils.isEmpty(complete) || TRUE.equals(complete)) {
            String id = request.getParameter(ID);
            ID typeId = null;
            if (!StringUtils.isEmpty(id)) {
                typeId = (ID) id;
            }
            return R.ok(new TreeEntityUtils<>(list).getTreeAll(typeId));
        } else {
            return R.ok(list);
        }
    }
}
