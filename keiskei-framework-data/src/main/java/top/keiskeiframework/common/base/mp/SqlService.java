package top.keiskeiframework.common.base.mp;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 * 执行原生SQL Service
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/16 17:49
 */

@Component
public class SqlService {
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(rollbackFor = Exception.class)
    public int executeSql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return query.executeUpdate();
    }

}
