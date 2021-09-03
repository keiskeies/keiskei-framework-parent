package top.keiskeiframework.common.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional(rollbackFor = Exception.class)
    public int executeSql(String sql) {
        return 0;
    }

}
