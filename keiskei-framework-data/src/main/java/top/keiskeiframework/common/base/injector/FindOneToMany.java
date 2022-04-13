package top.keiskeiframework.common.base.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import javax.persistence.OneToMany;
import java.lang.reflect.Field;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 18:11
 */
@Slf4j
public class FindOneToMany extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {


        Field[] fields = modelClass.getFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(OneToMany.class)) {
                log.info(field.getType().getName());
            }
        }

        String sql = "select * from " + tableInfo.getTableName();

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return this.addSelectMappedStatementForTable(mapperClass,
                "findOneToMany", sqlSource, tableInfo);
    }
}
