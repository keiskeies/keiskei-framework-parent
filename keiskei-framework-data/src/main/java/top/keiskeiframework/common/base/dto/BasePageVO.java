package top.keiskeiframework.common.base.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.keiskeiframework.common.base.entity.ListEntity;

import java.io.Serializable;

/**
 * @param <ID> .
 * @param <T>  .
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 统一请求封装
 * </p>
 * @since 2020/11/24 23:08
 */
@AllArgsConstructor
@NoArgsConstructor
public class BasePageVO<T extends ListEntity<ID>, ID extends Serializable> {

    /**
     * 分页参数
     */
    @Setter
    @Getter
    private Integer page = 1, size = 20;

    /**
     * 获取分页条件
     *
     * @return .
     */
    public Page<T> getPageable() {
        return new Page<>(page, size);
    }
}
