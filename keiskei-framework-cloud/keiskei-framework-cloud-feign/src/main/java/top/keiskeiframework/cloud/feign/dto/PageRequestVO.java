package top.keiskeiframework.cloud.feign.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author keiskei
 * @since 2022/12/6 22:33
 */
@Data
@NoArgsConstructor
public class PageRequestVO implements Serializable {
    private static final long serialVersionUID = 5639897110991198370L;

    private String desc;
    private String asc;
    private Boolean complete;
    private Boolean tree;
    private String show;
    private String conditions;
    private Long page;
    private Long offset;
    private Long size;
    private Boolean all;

    public PageRequestVO(BaseRequestVO request, BasePageVO pageVO) {
        this.desc = request.getDesc();
        this.asc = request.getAsc();
        this.complete = request.getComplete();
        this.tree = request.getTree();
        this.show = request.getShow();
        this.conditions = request.getConditions();
        this.all = pageVO.getAll();
        this.page = pageVO.getPage();
        this.offset = pageVO.getOffset();
        this.size = pageVO.getSize();
    }


}
