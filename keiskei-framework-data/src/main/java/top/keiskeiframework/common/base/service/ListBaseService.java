package top.keiskeiframework.common.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.ListEntity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 列表数据service
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author v_chenjiamin
 * @since 2022/4/15 15:36
 */
public interface ListBaseService<T extends ListEntity<ID>, ID extends Serializable> extends BaseService<T, ID>, IService<T> {

    /**
     * 完整数据详情
     *
     * @param id id
     * @return .
     */
    T getByIdComplete(Serializable id);

    /**
     * 完整数据列表查询
     *
     * @param request 列表条件
     * @param page    列表条件
     * @return .
     */
    Page<T> pageComplete(BaseRequestVO<T, ID> request, BasePageVO page);


    /**
     * 完整数据列表查询
     *
     * @param request 查询条件
     * @return 。
     */
    List<T> listComplete(BaseRequestVO<T, ID> request);

}
