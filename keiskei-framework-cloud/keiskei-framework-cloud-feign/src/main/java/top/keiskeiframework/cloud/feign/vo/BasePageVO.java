package top.keiskeiframework.cloud.feign.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BasePageVO implements Serializable {

    private static final long serialVersionUID = 6000957468735872254L;
    private Long page;
    private Long offset;
    private Long size;
}
