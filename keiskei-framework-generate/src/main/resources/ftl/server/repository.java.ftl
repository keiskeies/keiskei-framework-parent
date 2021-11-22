package ${module.packageName}.repository;

import ${module.packageName}.entity.${table.name};
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * ${table.comment!} DAO 层
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
@Repository
public interface ${table.name}Repository extends MongoRepository<${table.name}, String> {

}
