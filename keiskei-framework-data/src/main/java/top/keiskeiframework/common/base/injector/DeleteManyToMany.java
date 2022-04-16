package top.keiskeiframework.common.base.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.keiskeiframework.common.util.BeanUtils;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/12 18:11
 */
@Slf4j
public class DeleteManyToMany extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

        StringBuilder sqlSb = new StringBuilder("<script>");

        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null == manyToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null == joinTable) {
                continue;
            }
            log.info(field.getType().getName());
            Type t = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) t;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
            log.info(clz.getSimpleName());

            String firstId = BeanUtils.humpToUnderline(joinTable.joinColumns()[0].name());
            sqlSb.append("DELETE FROM ")
                    .append(joinTable.name())
                    .append(" WHERE ")
                    .append(firstId)
                    .append("=#{id};");

        }
        sqlSb.append("</script>");
        String sql = sqlSb.toString();
        log.info(sql);

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return this.addSelectMappedStatementForOther(mapperClass,
                "deleteManyToMany", sqlSource, null);
    }
}
