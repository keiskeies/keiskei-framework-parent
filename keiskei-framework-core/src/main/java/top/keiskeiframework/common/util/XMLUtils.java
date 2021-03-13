package top.keiskeiframework.common.util;

import top.keiskeiframework.common.enums.GlobalExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * xml工具类
 * @author James Chen right_way@foxmail.com
 * @since 2018年10月16日 下午2:40:58
 */
@SuppressWarnings("all")
@Slf4j
public class XMLUtils {

	/**
	 * 
	 * 将Java对象转换为xml
	 *
	 * @author James Chen right_way@foxmail.com
	 * @since 2017年8月13日 下午2:50:43
	 */
	public static String toXML(Object obj) {

		XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
		xstream.autodetectAnnotations(true);

		// 处理中文编码
		String temp = xstream.toXML(obj);

		try {
			String result = new String(temp.getBytes("UTF-8"), "ISO8859-1");
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new BizException(GlobalExceptionEnum.SERVER_ERROR.getCode(),
					GlobalExceptionEnum.SERVER_ERROR.getMsg());
		}

	}

	/**
	 * 
	 * 将Java对象转换为xml
	 *
	 * @author James Chen right_way@foxmail.com
	 * @since 2017年8月13日 下午2:50:43
	 * 
	 */
	public static String toXML(Object obj, String encode) {

		XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
		xstream.autodetectAnnotations(true);

		// 处理中文编码
		String temp = xstream.toXML(obj);

		try {
			String result = new String(temp.getBytes("UTF-8"), encode);
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new BizException(GlobalExceptionEnum.SERVER_ERROR.getCode(),
					GlobalExceptionEnum.SERVER_ERROR.getMsg());
		}

	}

	/**
	 * 
	 * 将Xml 转换为Java对象
	 *
	 * @author James Chen right_way@foxmail.com
	 * @since 2017年8月13日 下午2:54:39
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(String xml, Class<T> clazz) {

		XStream xs = new XStream(new DomDriver());

		xs.setMode(XStream.NO_REFERENCES);
		xs.ignoreUnknownElements();// 忽略不知道的字段
		xs.processAnnotations(new Class[] { clazz });

		return (T) xs.fromXML(xml);
	}

	/**
	 * 
	 * 把xml解析成一个map
	 *
	 * @author James Chen right_way@foxmail.com
	 * @since 2017年8月13日 下午2:55:35
	 */
	public static Map<String, String> xmlToMap(String xml) {

		Map<String, String> hashMap = null;

		try {
			Document document = DocumentHelper.parseText(xml);
			Element rootElement = document.getRootElement();

			hashMap = new HashMap<String, String>();

			for (Iterator it = rootElement.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				String name = element.getName();
				String text = element.getText();
				hashMap.put(name, text);
			}
		} catch (DocumentException e) {
			throw new BizException(GlobalExceptionEnum.SERVER_ERROR.getCode(),
					GlobalExceptionEnum.SERVER_ERROR.getMsg());
		}

		return hashMap;
	}

}
