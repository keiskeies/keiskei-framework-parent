package top.keiskeiframework.cache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

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
        String valueClassNam = value.getClass().getName();
        if (valueClassNam.endsWith(".PersistentArrayHolder")) {
            return Array.class.getName();
        } else if (
                valueClassNam.endsWith(".PersistentBag") ||
                        valueClassNam.endsWith(".PersistentIdentifierBag") ||
                        valueClassNam.endsWith(".PersistentList")

        ) {
            return ArrayList.class.getName();
        } else if (valueClassNam.endsWith(".PersistentSortedMap")) {
            return TreeMap.class.getName();
        } else if (valueClassNam.endsWith(".PersistentSortedSet")) {
            return TreeSet.class.getName();
        } else if (valueClassNam.endsWith(".PersistentMap")) {
            return HashMap.class.getName();
        } else if (valueClassNam.endsWith(".PersistentSet")) {
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

