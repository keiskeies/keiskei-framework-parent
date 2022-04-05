package top.keiskeiframework.cloud.feign.front.service;

import org.springframework.data.domain.Page;
import top.keiskeiframework.cloud.feign.dto.BasePageDTO;
import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 基础服务接口
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface IListFrontService<T extends ListEntityDTO<ID>, ID extends Serializable> extends IFrontService<T, ID> {


    /**
     * 分页查询
     *
     * @param request 查询条件
     * @param page    分页条件
     * @return 。
     */
    Page<T> page(BaseRequestDTO<T, ID> request, BasePageDTO<T, ID> page);

    /**
     * 下拉框
     *
     * @param request 查询条件
     * @param page    分页信息
     * @return 。
     */
    List<T> options(BaseRequestDTO<T, ID> request, BasePageDTO<T, ID> page);

    /**
     * 全部下卡框
     *
     * @param request 查询条件
     * @return 。
     */
    List<T> all(BaseRequestDTO<T, ID> request);

}
