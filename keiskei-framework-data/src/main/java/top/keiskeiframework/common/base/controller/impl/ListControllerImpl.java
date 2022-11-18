package top.keiskeiframework.common.base.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import top.keiskeiframework.common.base.controller.IControllerService;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.service.IListBaseService;

import java.io.Serializable;

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
public class ListControllerImpl<T extends IListEntity<ID>, ID extends Serializable>
        extends AbstractControllerServiceImpl<T, ID>
        implements IControllerService<T, ID> {

    @Autowired
    private IListBaseService<T, ID> listBaseService;

    @GetMapping("/export")
    public void export(BaseRequestVO<T, ID> request) {

    }


}
