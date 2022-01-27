package top.keiskeiframework.demo.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.demo.goods.entity.Goods;
import top.keiskeiframework.demo.goods.entity.GoodsDetail;

/**
 * <p>
 * 接口
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 18:50
 */
public interface GoodsDetailRepository extends JpaRepository<GoodsDetail, Long>, JpaSpecificationExecutor<GoodsDetail> {
}
