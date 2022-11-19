package top.keiskeiframework.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/18 15:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDepartment implements Serializable {

    private static final long serialVersionUID = 8587257400808871585L;

    private Integer id;

    private String name;
}
