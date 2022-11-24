package top.keiskeiframework.cloud.feign.front.service.impl;

import lombok.extern.slf4j.Slf4j;
import top.keiskeiframework.cloud.feign.front.service.ITreeFrontService;
import top.keiskeiframework.cloud.feign.front.util.TreeEntityDtoUtils;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.ITreeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class TreeFrontServiceImpl<DTO extends ITreeEntity<ID>, T extends ITreeEntity<ID>, ID extends Serializable>
        extends AbstractBaseFrontServiceImpl<DTO, T, ID>
        implements ITreeFrontService<DTO, ID> {

    @Override
    public PageResultVO<DTO> page(BaseRequestVO<DTO, ID> requestVO, BasePageVO pageVO) {
        PageResultVO<DTO> tiPage =  super.page(requestVO, pageVO);
        if (requestVO.getTree()) {
            List<DTO> treeList = new TreeEntityDtoUtils<>(tiPage.getData()).getTreeAll();
            tiPage.setData(treeList);
        }
        return tiPage;
    }

    @Override
    public List<DTO> options(BaseRequestVO<DTO, ID> requestVO) {
        List<DTO> noTreeData = super.options(requestVO);
        if (requestVO.getTree()) {
            return new TreeEntityDtoUtils<>(noTreeData).getTreeAll();
        }
        return noTreeData;
    }
}
