package top.keiskeiframework.common.base;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 执行原生SQL Service
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/16 17:49
 */

@Component
public class SqlService {
    @PersistenceContext
    EntityManager em;

    public List<Map<String, Object>> queryBySql(String sql) {
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }


    @Transactional(rollbackFor = Exception.class)
    public int executeSql(String sql) {
        Query query = em.createNativeQuery(sql);
        return query.executeUpdate();
    }

}
