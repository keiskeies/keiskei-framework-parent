package top.keiskeiframework.cache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

/**
 * <p>
 * hibernate 查询集合转换
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/14 13:48
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS
)
@JsonTypeIdResolver(value = HibernateCollectionIdResolver.class)
public class HibernateCollectionMixIn {
}
