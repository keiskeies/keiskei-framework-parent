package top.keiskeiframework.common.cache.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.SerializationUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/13 17:09
 */
public class HibernateGenericJackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {
    static final byte[] EMPTY_ARRAY = new byte[0];

    public HibernateGenericJackson2JsonRedisSerializer() {
        super();
    }

    public HibernateGenericJackson2JsonRedisSerializer(String classPropertyTypeName) {
        super(classPropertyTypeName);
    }

    public HibernateGenericJackson2JsonRedisSerializer(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public byte[] serialize(Object source) throws SerializationException {
        if (source == null) {
            return EMPTY_ARRAY;
        } else {
//            try {
//                return super.mapper.writeValueAsBytes(source);
//            } catch (JsonProcessingException var3) {
//                throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
//            }
        }
        return super.serialize(source);
    }

    @Override
    public Object deserialize(byte[] source) throws SerializationException {
        return super.deserialize(source);
    }

    @Override
    public <T> T deserialize(byte[] source, Class<T> type) throws SerializationException {
        return super.deserialize(source, type);
    }
}
