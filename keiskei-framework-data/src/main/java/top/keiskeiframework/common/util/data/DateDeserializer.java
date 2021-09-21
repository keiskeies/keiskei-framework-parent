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
 * @since 2021/9/21 21:26
 */
public class DateDeserializer extends StdConverter<String, Date> {
    @Override
    public Date convert(String value) {
        return DateUtils.parseDatetime(value);
    }
}
