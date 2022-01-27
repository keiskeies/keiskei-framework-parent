package top.keiskeiframework.demo.goods.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.demo.goods.entity.GoodsClassify;
import top.keiskeiframework.demo.goods.entity.GoodsClassifyDetail;
import top.keiskeiframework.demo.goods.service.IGoodsClassifyDetailService;
import top.keiskeiframework.demo.goods.service.IGoodsClassifyService;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 18:55
 */
@Service
public class GoodsClassifyDetailServiceImpl extends ListServiceImpl<GoodsClassifyDetail, Long> implements IGoodsClassifyDetailService {
}
