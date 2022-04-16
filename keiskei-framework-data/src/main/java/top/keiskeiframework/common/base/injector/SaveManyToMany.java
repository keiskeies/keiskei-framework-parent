package top.keiskeiframework.common.base.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.keiskeiframework.common.base.injector.dto.ManyToManyResult;
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
public class SaveManyToMany extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

        StringBuilder sqlSb = new StringBuilder();

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

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(joinTable.joinColumns()[0].name());
            String firstTableId = joinTable.joinColumns()[0].referencedColumnName();
            String secondId = BeanUtils.humpToUnderline(joinTable.inverseJoinColumns()[0].name());

            sqlSb.append(";INSERT INTO ").append(joinTable.name()).append(" (").append(firstId).append(", ").append(secondId).append(") VALUES <foreach collection=\"").append(field.getName()).append("\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\"> #{id}, #{item.id}</foreach>");
        }
        sqlSb.append("</script>");
        String sql = "<script>" + sqlSb.toString().replaceFirst(";", "");
        log.info(sql);

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return this.addSelectMappedStatementForOther(mapperClass,
                "saveManyToMany", sqlSource, null);
    }
}