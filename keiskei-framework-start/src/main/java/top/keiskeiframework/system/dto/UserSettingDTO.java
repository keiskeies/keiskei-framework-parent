package top.keiskeiframework.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/18 17:13
 */
@Data
public class UserSettingDTO implements Serializable {

    private static final long serialVersionUID = 6876195333611435675L;
    private Integer id;

    private String size;

    private String theme;

    private Boolean showSettings;

    private Boolean tagsView;

    private Boolean fixedHeader;

    private Boolean sidebarLogo;
}
