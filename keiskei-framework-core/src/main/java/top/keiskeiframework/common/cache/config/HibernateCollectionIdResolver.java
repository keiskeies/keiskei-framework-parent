package top.keiskeiframework.common.cache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import org.hibernate.collection.internal.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>
 * hibernate 集合类型转换
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/14 13:45
 */
public class HibernateCollectionIdResolver extends TypeIdResolverBase {

    public HibernateCollectionIdResolver() {
    }

    @Override
    public String idFromValue(Object value) {
        if (value instanceof PersistentArrayHolder) {
            return Array.class.getName();
        } else if (value instanceof PersistentBag || value instanceof PersistentIdentifierBag || value instanceof PersistentList) {
            return ArrayList.class.getName();
        } else if (value instanceof PersistentSortedMap) {
            return TreeMap.class.getName();
        } else if (value instanceof PersistentSortedSet) {
            return TreeSet.class.getName();
        } else if (value instanceof PersistentMap) {
            return HashMap.class.getName();
        } else if (value instanceof PersistentSet) {
            return HashSet.class.getName();
        } else {
            return value.getClass().getName();
        }
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JavaType typeFromId(DatabindContext ctx, String id) {
        try {
            return ctx.getConfig().constructType(Class.forName(id));
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CLASS;
    }
}

