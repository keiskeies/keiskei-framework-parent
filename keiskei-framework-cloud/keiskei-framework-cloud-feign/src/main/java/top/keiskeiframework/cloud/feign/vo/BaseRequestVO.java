package top.keiskeiframework.cloud.feign.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@SuperBuilder
public class BaseRequestVO implements Serializable {

    private static final long serialVersionUID = -1326632861685692059L;

    private String desc;
    private String asc;
    private Boolean complete;
    private Boolean tree;
    private String conditions;
    private String show;

    public BaseRequestVO() {
        this.complete = Boolean.FALSE;
        this.tree = Boolean.TRUE;
    }

}
