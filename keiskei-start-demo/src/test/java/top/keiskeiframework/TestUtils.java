package top.keiskeiframework;

import io.swagger.annotations.ApiModelProperty;
import top.keiskeiframework.content.page.entity.Topic;

import java.lang.reflect.Field;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/18 00:08
 */
public class TestUtils {
    public static void main(String[] args) {
        Field[] fields = Topic.class.getDeclaredFields();
        for (Field field : fields) {
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            System.out.println("{ show: true, edit: true, queryFlag: false, sortable: false, minWidth: 300, key: '" + field.getName() + "', label: '" +

                    (null == apiModelProperty ? field.getName() : apiModelProperty.value())
                    + "' },");
        }
    }
}
