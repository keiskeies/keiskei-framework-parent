package top.keiskeiframework.dashboard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.keiskeiframework.common.base.service.IListBaseService;
import top.keiskeiframework.common.vo.dashboard.charts.ChartOptionVO;
import top.keiskeiframework.dashboard.entity.Dashboard;

/**
 * <p>
 * 图表
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 18:39
 */
public interface IDashboardService extends IListBaseService<Dashboard, Integer>, IService<Dashboard> {

    /**
     * 获取图表及结构
     * @param id 图表ID
     * @return 。
     */
    ChartOptionVO getChartOption(Integer id);
    /**
     * 获取图表及结构
     * @param id 图表ID
     * @return 。
     */
    ChartOptionVO refreshChartOption(Integer id);

}
