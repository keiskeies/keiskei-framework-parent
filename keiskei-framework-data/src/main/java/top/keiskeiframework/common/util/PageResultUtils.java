package top.keiskeiframework.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.PageResultVO;

import java.util.List;

/**
 * <p>
 * 分页结果转换
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/14 23:58
 */
public class PageResultUtils {

    public static <T> PageResultVO<T> of(Page<T> page) {
        Pageable pageable = page.getPageable();
        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setPage((long) pageable.getPageNumber());
        pageResultVO.setSize((long) page.getSize());
        pageResultVO.setTotal(page.getTotalElements());
        pageResultVO.setData(page.getContent());
        return pageResultVO;
    }

    public static <T> PageResultVO<T> of(IPage<T> page) {
        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setPage(page.getPages());
        pageResultVO.setSize(page.getSize());
        pageResultVO.setTotal(page.getTotal());
        pageResultVO.setData(page.getRecords());
        return pageResultVO;
    }

    public static <T> PageResultVO<T> of(List<T> list) {
        int total = CollectionUtils.isEmpty(list) ? 0 : list.size();
        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setData(list);
        pageResultVO.setSize((long)total);
        pageResultVO.setTotal((long)total);
        return pageResultVO;
    }

    public static <T> PageResultVO<T> of(BasePageVO basePageVO) {
        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setPage(basePageVO.getPage());
        pageResultVO.setSize(basePageVO.getSize());
        pageResultVO.setOffset(basePageVO.getOffset());
        return pageResultVO;
    }

}
