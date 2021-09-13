package top.keiskeiframework.info.page.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.info.page.entity.Article;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:57
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
}
