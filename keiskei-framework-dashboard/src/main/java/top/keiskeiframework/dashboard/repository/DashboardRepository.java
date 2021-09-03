package top.keiskeiframework.dashboard.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.dashboard.entity.Dashboard;

/**
 * <p>
 * 图表 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Repository
public interface DashboardRepository extends MongoRepository<Dashboard, ObjectId> {


}
