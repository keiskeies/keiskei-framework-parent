package top.keiskeiframework.demo.goods.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.demo.goods.entity.Goods;
import top.keiskeiframework.demo.goods.entity.GoodsImage;
import top.keiskeiframework.demo.goods.service.IGoodsImageService;
import top.keiskeiframework.demo.goods.service.IGoodsService;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 18:55
 */
@Service
public class GoodsImageServiceImpl extends ListServiceImpl<GoodsImage, Long> implements IGoodsImageService {
}
