package top.keiskeiframework.dashboard.service;

import top.keiskeiframework.common.base.service.IBaseService;
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
public interface IDashboardService extends IBaseService<Dashboard, Integer> {

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