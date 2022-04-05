package top.keiskeiframework.common.base.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.data.domain.Sort;

import java.io.IOException;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/4 12:54
 */
public class BaseSortDeserializer extends JsonDeserializer<Sort> {
    @Override
    public Sort deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ArrayNode node = jp.getCodec().readTree(jp);
        Sort.Order[] orders = new Sort.Order[node.size()];
        int i = 0;
        for (JsonNode obj : node) {
            orders[i] = new Sort.Order(Sort.Direction.valueOf(obj.get("direction").asText()), obj.get("property").asText());
            i++;
        }
        return Sort.by(orders);
    }
}