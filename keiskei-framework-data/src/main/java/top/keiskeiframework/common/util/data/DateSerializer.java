package top.keiskeiframework.common.util.data;

import com.fasterxml.jackson.databind.util.StdConverter;
import top.keiskeiframework.common.util.DateUtils;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/21 21:20
 */
public class DateSerializer extends StdConverter<Date, String> {
    @Override
    public String convert(Date value) {
        return DateUtils.formatDatetime(value);
    }
}
