package ${module.packageName}.repository;

import ${module.packageName}.entity.${table.name};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * ${table.comment!} DAO 层
 * </p>
 *
 * @author ${author}
 * @since ${since}
 */
public interface ${table.name}Repository extends JpaRepository<${table.name}, ${table.idType.value}>, JpaSpecificationExecutor<${table.name}> {

}
