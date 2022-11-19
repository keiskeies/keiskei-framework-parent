package top.keiskeiframework.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户部门
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 21:32
 */
@Data
public class UserDepartmentDTO implements Serializable {
    private static final long serialVersionUID = 1023875847860863207L;
    private Integer id;
    private String name;
}
