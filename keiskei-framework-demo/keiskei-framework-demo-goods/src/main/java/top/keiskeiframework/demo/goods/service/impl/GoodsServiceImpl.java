package top.keiskeiframework.demo.goods.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.demo.goods.entity.Goods;
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
public class GoodsServiceImpl extends ListServiceImpl<Goods, Long> implements IGoodsService {
}