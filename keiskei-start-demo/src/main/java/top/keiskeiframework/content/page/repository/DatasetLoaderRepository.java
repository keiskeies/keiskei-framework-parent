package top.keiskeiframework.content.page.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.content.page.entity.DatasetLoader;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:57
 */
@Repository
public interface DatasetLoaderRepository extends MongoRepository<DatasetLoader, String> {
    /**
     * 通过datasetID查询
     *
     * @param datasetId .
     * @return .
     */
    List<DatasetLoader> findAllByDatasetId(String datasetId);
}
