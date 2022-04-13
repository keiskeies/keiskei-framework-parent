package top.keiskeiframework.generate.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
    LOCAL(0,"keiskei-framework-file-local"),
    ALI_OSS(1,"keiskei-framework-file-aliass"),
    JD_OSS(2,"keiskei-framework-file-jdoss")
    ;


    @EnumValue
    private final int code;
    private final String value;
}
