package top.keiskeiframework.demo.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.demo.goods.entity.GoodsImage;

/**
 * <p>
 * 接口
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/1/25 18:50
 */
public interface GoodsImageRepository extends JpaRepository<GoodsImage, Long>, JpaSpecificationExecutor<GoodsImage> {
}
