package top.keiskeiframework.generate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 文件管理方式选择
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/8/25 23:03
 */
@Getter
@AllArgsConstructor
public enum ProjectInfoFileJarEnum {
    //
    LOCAL("keiskei-framework-file-local"),
    ALI_OSS("keiskei-framework-file-aliass"),
    JD_OSS("keiskei-framework-file-jdoss")
    ;

    private final String value;
}
