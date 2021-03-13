package top.keiskeiframework.generate.dto;

import lombok.Data;

import java.util.List;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/11 13:27
 */
@Data
public class ModuleDTO {

    private String name;
    private String packageName;
    private String comment;
    private List<EntityDTO> entities;
}
