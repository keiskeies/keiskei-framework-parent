package top.keiskeiframework.generate.dto;

import lombok.Data;

import java.util.List;
import java.util.Random;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/11 13:28
 */
@Data
public class EntityDTO {
    private String name;
    private String table;
    private String comment;

    public String getSerialVersionUID() {
        Random random = new Random();

        return random.nextLong() * (~random.nextInt(5)) + "";
    }

    private List<FieldDTO> fields;
}
