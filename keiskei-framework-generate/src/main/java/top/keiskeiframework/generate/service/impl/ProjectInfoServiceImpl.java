package top.keiskeiframework.generate.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.generate.entity.ProjectInfo;
import top.keiskeiframework.generate.mapper.ProjectInfoMapper;
import top.keiskeiframework.generate.service.IProjectInfoService;

/**
 * <p>
 * 项目信息 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class ProjectInfoServiceImpl extends ListServiceImpl<ProjectInfo, Integer, ProjectInfoMapper> implements IProjectInfoService {
}
