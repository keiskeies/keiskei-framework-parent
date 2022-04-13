package top.keiskeiframework.common.base.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 18:11
 */
public class FindManyToOne extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

        String sql = "select * from " + tableInfo.getTableName();

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return this.addSelectMappedStatementForTable(mapperClass,
                "findManyToOne", sqlSource, tableInfo);
    }
}
