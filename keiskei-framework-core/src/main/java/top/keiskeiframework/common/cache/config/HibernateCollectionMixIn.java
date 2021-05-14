package top.keiskeiframework.common.cache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

/**
 * <p>
 *
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
