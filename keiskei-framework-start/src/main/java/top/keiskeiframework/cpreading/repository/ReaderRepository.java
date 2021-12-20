package top.keiskeiframework.cpreading.repository;

import top.keiskeiframework.cpreading.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 读者管理 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface ReaderRepository extends JpaRepository<Reader, Long>, JpaSpecificationExecutor<Reader> {

    Reader findTopByWechatWebOpenid(String wechatWebOpenId);
    Reader findTopByWechatMiniOpenid(String wechatMiniOpenId);
    Reader findTopByUnionid(String unionid);


}
