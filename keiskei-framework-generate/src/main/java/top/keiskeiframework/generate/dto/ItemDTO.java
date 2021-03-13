package top.keiskeiframework.generate.dto;

import lombok.Data;
import top.keiskeiframework.common.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/11 13:25
 */
@Data
public class ItemDTO {
    private String name;
    private String comment;
    private String author;
    private String since;

    public String getSince() {
        return null == this.since ? DateTimeUtils.timeToString(LocalDateTime.now()) : this.since;
    }


    private List<ModuleDTO> modules;


}
